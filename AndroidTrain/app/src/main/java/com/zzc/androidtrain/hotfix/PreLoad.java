package com.zzc.androidtrain.hotfix;

/**
 * 辅助类
 *
 * 该类被单独放在一个dex中
 * 该类被其他所有类所引用
 * 防止其他类在dex优化过程中被打上CLASS_ISPREVERIFIED标签
 *
 * Created by zczhang on 16/7/14.
 */
public class PreLoad {
}
