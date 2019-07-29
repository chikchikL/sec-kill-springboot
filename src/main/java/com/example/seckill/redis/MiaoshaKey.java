package com.example.seckill.redis;

public class MiaoshaKey extends BasePrefix{


    private MiaoshaKey(int expireSeconds, String prefix) {
		super(expireSeconds,prefix);
	}
	public static MiaoshaKey isGoodsOver = new MiaoshaKey(0,"go");

	//uuid的有效期，设置为60秒
	public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60,"mp");

	//验证码有效时间5分钟
	public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300, "vc");

}
