package com.example.seckill.controller;

import com.example.seckill.Vo.GoodsVo;
import com.example.seckill.domain.MiaoshaMessage;
import com.example.seckill.domain.MiaoshaOrder;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.domain.OrderInfo;
import com.example.seckill.rabbitmq.MQSender;
import com.example.seckill.redis.GoodsKey;
import com.example.seckill.result.CodeMsg;
import com.example.seckill.result.Result;
import com.example.seckill.service.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.CoderMalfunctionError;
import java.util.List;

@Controller
@RequestMapping("miaosha")
public class MiaoshaController implements InitializingBean {


    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    MQSender sender;

    //因为涉及数据库插入数据，应该用post方法
    //get post区别
    //get 幂等性，无论调用多少次对服务端数据无影响
    //post
    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody//返回秒杀的订单信息
    public Result<Integer> miaosha(Model model, MiaoshaUser user,
                                     @RequestParam("goodsId")Long goodsId){
        model.addAttribute("user",user);

        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //redis预减少库存，如果已经秒杀完了，直接返回
        Long remain = redisService.decr(GoodsKey.getMiaoshaGoodsStock, String.valueOf(goodsId));
        if(remain < 0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //判断是否已经秒杀到了，若是返回重复秒杀
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(miaoshaOrder!=null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //如果没有秒杀到过，发送到消息队列
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setUser(user);
        miaoshaMessage.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(miaoshaMessage);

        //返回排队成功
        return Result.success(0);

        /**
         * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
         */
      /*  //判断商品库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stock = goods.getStockCount();

        if(stock <= 0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }


        //判断是否秒杀成功
        MiaoshaOrder miaoshaOrder =
                orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);

        if(miaoshaOrder != null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //减库存 下订单 写入秒杀订单（事务）
        //秒杀需要的是秒杀用户和商品信息生成订单信息
        //因为秒杀成功后会进入订单详情页面
        OrderInfo orderInfo = miaoshaService.miaosha(user,goods);

        return Result.success(orderInfo);
*/
    }

    /**
     * 系统初始化时的回调
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        if(goodsVos == null)
            return;

        //系统初始化将秒杀商品的库存加载到redis
        for(GoodsVo vo:goodsVos){
            redisService.set(GoodsKey.getMiaoshaGoodsStock,String.valueOf(vo.getId()),vo.getStockCount());
        }
    }

    /**
     * 请求do_miaosha接口后，若成功发送到消息队列，那么由前台发送请求获取秒杀结果
     * @return orderId：成功，-1：失败 0：排队中
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody//返回秒杀的订单信息
    public Result<Long> result(Model model, MiaoshaUser user,
                                   @RequestParam("goodsId")Long goodsId) {
        model.addAttribute("user", user);

        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        Long miaoshaResult = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(miaoshaResult);
    }
}
