/**
 * Created by Accident on 09/06/2017.
 */
let no_daily_data = [];
let before_daily_data = [];
let after_daily_data = [];

function get_MA_data(KLineValue) {
    let result = [];
    for(let i = 0; i < KLineValue.length; i++) {
        result.push(KLineValue[i][1]);
    }
    return result;
}

function get_no_KLineData(rawData) {
    return {
        categoryData: rawData.categoryData,
        KLineValue: rawData.KLineValue_no_adj,
        adjs: get_MA_data(rawData.KLineValue_no_adj),
        changeRates: rawData.changeRates,
        volumns: rawData.volumns,
        turnOverRates: rawData.turnOverRates,
        kIndexes: rawData.kIndexes,
        dIndexes: rawData.dIndexes,
        jIndexes: rawData.jIndexes,
        difs: rawData.difs,
        deas: rawData.deas,
        macds: rawData.macds,
        rsi6s: rawData.rsi6s,
        rsi12s: rawData.rsi12s,
        rsi24s: rawData.rsi24s,
        BIAS6: rawData.BIAS6,
        BIAS12: rawData.BIAS12,
        BIAS24: rawData.BIAS24,
        Boll: rawData.Boll,
        upper: rawData.upper,
        lower: rawData.lower,
        WR1: rawData.WR1,
        WR2: rawData.WR2
    };
}

function get_before_KLineData(rawData) {
    return {
        categoryData: rawData.categoryData,
        KLineValue: rawData.KLineValue_before_adj,
        adjs: get_MA_data(rawData.KLineValue_before_adj),
        changeRates: rawData.changeRates,
        volumns: rawData.volumns,
        turnOverRates: rawData.turnOverRates,
        kIndexes: rawData.kIndexes,
        dIndexes: rawData.dIndexes,
        jIndexes: rawData.jIndexes,
        difs: rawData.difs_before_adj,
        deas: rawData.deas_before_adj,
        macds: rawData.macds_before_adj,
        rsi6s: rawData.rsi6s,
        rsi12s: rawData.rsi12s,
        rsi24s: rawData.rsi24s,
        BIAS6: rawData.BIAS6,
        BIAS12: rawData.BIAS12,
        BIAS24: rawData.BIAS24,
        Boll: rawData.Boll_before_adj,
        upper: rawData.upper_before_adj,
        lower: rawData.lower_before_adj,
        WR1: rawData.WR1,
        WR2: rawData.WR2
    };
}

function get_after_KLineData(rawData) {
    return {
        categoryData: rawData.categoryData,
        KLineValue: rawData.KLineValue_after_adj,
        adjs: get_MA_data(rawData.KLineValue_after_adj),
        changeRates: rawData.changeRates,
        volumns: rawData.volumns,
        turnOverRates: rawData.turnOverRates,
        kIndexes: rawData.kIndexes,
        dIndexes: rawData.dIndexes,
        jIndexes: rawData.jIndexes,
        difs: rawData.difs_after_adj,
        deas: rawData.deas_after_adj,
        macds: rawData.macds_after_adj,
        rsi6s: rawData.rsi6s,
        rsi12s: rawData.rsi12s,
        rsi24s: rawData.rsi24s,
        BIAS6: rawData.BIAS6,
        BIAS12: rawData.BIAS12,
        BIAS24: rawData.BIAS24,
        Boll: rawData.Boll_after_adj,
        upper: rawData.upper_after_adj,
        lower: rawData.lower_after_adj,
        WR1: rawData.WR1,
        WR2: rawData.WR2
    };
}

function setDailyData(data) {
    no_daily_data = get_no_KLineData(data);
    before_daily_data = get_before_KLineData(data);
    after_daily_data = get_after_KLineData(data);
}