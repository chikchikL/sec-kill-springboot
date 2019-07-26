package com.example.seckill.controller;

import com.example.seckill.Vo.GoodsVo;
import com.example.seckill.domain.MiaoshaUser;
import com.example.seckill.redis.GoodsKey;
import com.example.seckill.service.GoodsService;
import com.example.seckill.service.MiaoshaUserService;
import com.example.seckill.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

        //查询商品列表
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodslist",goodsVos);

//        return "goods_list";
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

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

    @RequestMapping("/to_detail/{goodsId}")
    public String toGoodDetail(Model model, MiaoshaUser user,
                               @PathVariable("goodsId")Long goodsId){
        model.addAttribute("user",user);

        //snowflake生成uniq_id，防止因为自增的主键造成数据库很容易遍历
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

        return "goods_detail";
    }



}
