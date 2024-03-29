package com.example.seckill.controller;

import com.example.seckill.Vo.GoodsDetailVo;
import com.example.seckill.Vo.GoodsVo;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.redis.GoodsKey;
import com.example.seckill.result.Result;
import com.example.seckill.service.GoodsService;
import com.example.seckill.service.MiaoshaUserService;
import com.example.seckill.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodController {


    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping("/to_list")
    @ResponseBody
    public String toGoodList(HttpServletRequest request, HttpServletResponse response,
                             Model model, MiaoshaUser user){
        model.addAttribute("user",user);

//      return "goods_list";
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        //查询商品列表
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodslist",goodsVos);

        //无缓存手动渲染
        WebContext springWebContext = new WebContext(request, response,
                request.getServletContext(),
                request.getLocale(), model.asMap());

        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", springWebContext);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }

        return html;
    }

    /**
     * 前后端分离，只返回json对象
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toGoodDetail(HttpServletRequest request, HttpServletResponse response,
                                              Model model, MiaoshaUser user,
                                              @PathVariable("goodsId")Long goodsId){

        //商用中使用snowflake生成uniq_id，防止因为自增的主键造成数据库很容易遍历
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();


        int miaoshaStatus = 0;
        int remainSeconds = 0;

        if(now < startTime){
            //秒杀未开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)(startTime - now)/1000;

        }else if(now > endTime){
            //秒杀结束
            miaoshaStatus = 2;
            remainSeconds = -1;

        }else{
            //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setMiaoshaStatus(miaoshaStatus);
        vo.setRemainSeconds(remainSeconds);

        return Result.success(vo);
    }


    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String toGoodDetail2(HttpServletRequest request, HttpServletResponse response,
                               Model model, MiaoshaUser user,
                               @PathVariable("goodsId")Long goodsId){
        model.addAttribute("user",user);


        //尝试取缓存,
        //这里需要指定key为goodsId，商品列表不需要指定，但是详情页需要根据url中的商品id进行区别
        /**
         * 区分缓存粒度：商品列表-》页面缓存，商品详情-》url缓存
         */
        String html = redisService.get(GoodsKey.getGoodsDetail,String.valueOf(goodsId), String.class);
        if(!StringUtils.isEmpty(html))
            return html;


        //商用中使用snowflake生成uniq_id，防止因为自增的主键造成数据库很容易遍历
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();


        int miaoshaStatus = 0;
        int remainSeconds = 0;

        if(now < startTime){
            //秒杀未开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)(startTime - now)/1000;

        }else if(now > endTime){
            //秒杀结束
            miaoshaStatus = 2;
            remainSeconds = -1;

        }else{
            //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("goods",goods);

        //无缓存手动渲染并添加缓存
        WebContext springWebContext = new WebContext(request, response,
                request.getServletContext(),
                request.getLocale(), model.asMap());

        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", springWebContext);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,String.valueOf(goodsId),html);
        }

        return html;
    }
}
