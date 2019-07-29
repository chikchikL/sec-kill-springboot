package com.example.seckill.service;


import com.example.seckill.Vo.GoodsVo;
import com.example.seckill.domain.MiaoshaOrder;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.domain.OrderInfo;
import com.example.seckill.redis.MiaoshaKey;
import com.example.seckill.util.MD5Util;
import com.example.seckill.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    /**
     * 秒杀事务 减库存 下订单 写入秒杀订单（事务）
     */
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {

        //减少库存
        boolean success = goodsService.reduceStock(goods);

        //只有成功减少库存才创建订单
        if(!success){
            setGoodsOver(goods.getId());
            return  null;
        }
        //下订单 写入秒杀订单
        return orderService.createOrder(user,goods);
    }

    //设置秒杀结束缓存标记，用于客户端轮询秒杀结果时，判断是排队还是秒杀失败
    private void setGoodsOver(Long id) {

        redisService.set(MiaoshaKey.isGoodsOver,String.valueOf(id),true);
    }


    private boolean getGoodsOver(Long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver,String.valueOf(goodsId));
    }


    /**
     * 客户端轮询获取秒杀结果的业务逻辑，这里的逻辑在于，最多往消息队列中插入stock个消息请求
     * 所以每个消息被成功消费时，都是可以轮询秒杀请求的结果的，只要没秒杀成功或者秒杀
     * 完代表当前请求还有机会
     * @param id
     * @param goodsId
     * @return
     */
    public long getMiaoshaResult(Long id, Long goodsId) {
        MiaoshaOrder miaoshaorder = orderService.getMiaoshaOrderByUserIdGoodsId(id, goodsId);
        if(miaoshaorder != null)
            return miaoshaorder.getOrderId();
        else{
            //秒杀失败或者排队
             boolean isOver = getGoodsOver(goodsId);
             if(isOver){
                 return -1;
             }else{
                 return 0;
             }
        }
    }


    public void reset(List<GoodsVo> goodsVos) {
        goodsService.resetStock(goodsVos);
        orderService.deleteOrders();
    }


    /**
     * 验证请求的path是否正确
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    public boolean checkPath(MiaoshaUser user, Long goodsId, String path) {

        if(user == null || path == null)
            return false;



        String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, user.getId() + "_" + goodsId, String.class);

        return path.equals(pathOld);
    }

    public String createPath(MiaoshaUser user, Long goodsId) {

        String path = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(MiaoshaKey.getMiaoshaPath,user.getId() +"_"+ goodsId,path);
        return path;
    }

    /**
     * 生成验证码
     * @param user
     * @param goodsId
     * @return
     */
    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public boolean checkVerifyCode(MiaoshaUser user, Long goodsId, Integer verifyCode) {

        if(user == null || goodsId <= 0)
            return false;

        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId, Integer.class);
        if(codeOld == null || codeOld != verifyCode){
            return  false;
        }

        //验证成功后，需要删除验证过的验证码，防止下次还可以用
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode,user.getId()+","+goodsId);

        return true;
    }
}
