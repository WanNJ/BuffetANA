package driver;

import gui.utils.CodeComplementUtil;

/**
 * Created by wshwbluebird on 2017/3/15.
 */


//测试自动补全 逻辑单例
public class testComp  {
    public static void main(String[] args){
        CodeComplementUtil codeComplementUtil  =CodeComplementUtil.CODE_COMPLEMENT_UTIL;
        codeComplementUtil.getComplement("30").stream().forEach(t-> System.out.println(t));
    }

}
