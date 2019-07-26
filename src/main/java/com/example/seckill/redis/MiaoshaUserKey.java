package com.example.seckill.redis;

public class MiaoshaUserKey extends BasePrefix{

	public static final int TOKEN_EXPIRE = 3600*24 * 2;
	private MiaoshaUserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "tk");
	//因为是对应的数据库中的User对象，因此设置为0永久缓存
	public static MiaoshaUserKey id = new MiaoshaUserKey(0, "id");

}
