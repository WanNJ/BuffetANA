package util;

/**
 * Created by wshwbluebird on 2017/4/16.
 */
public enum SaveResult {
    Success, //保存成功
    fail,   //失败因为不明原因
    Already ,//已经存在，询问是否覆盖
    Modify , // 修改成功
    NameError //这种名字已经在另一种策略中用过 必须更改
}
