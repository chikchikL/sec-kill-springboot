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

    /**
     * 减少库存，并判断是否减少成功
     *
     * @param goods
     * @return
     */
    public boolean reduceStock(GoodsVo goods){
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int ret = goodsDao.reduceStock(g);
        return ret>0;
    }

}
