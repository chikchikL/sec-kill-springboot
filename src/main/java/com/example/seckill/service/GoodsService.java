package com.example.seckill.service;

import com.example.seckill.Vo.GoodsVo;
import com.example.seckill.dao.GoodsDao;
import com.example.seckill.domain.Goods;
import com.example.seckill.domain.MiaoshaGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(Long goodsId) {

        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(Goods goods){
        MiaoshaGoods g = new MiaoshaGoods();
        g.setId(goods.getId());
        g.setStockCount(goods.getGoodsStock() - 1);
        goodsDao.reduceStock(g);
    }

}
