/**
 * Created by Accident on 09/06/2017.
 */
let dailyData = [];
let weeklyData = [];
let monthlyData = [];

function get_MA_data(KLineValue) {
    let result = [];
    for(let i = 0; i < KLineValue.length; i++) {
        result.push(KLineValue[i][1]);
    }
    return result;
}

function getKLineData(rawData) {
    return {
        categoryData: rawData.categoryData,
        KLineValue: rawData.KLineValue,
        adjs: get_MA_data(rawData.KLineValue),
        changeRates: rawData.changeRates,
        volumns: rawData.volumns,
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

function setDailyData(data) {
    dailyData = getKLineData(data);
}

function setWeeklyData(data) {
    weeklyData = getKLineData(data);
}

function setMonthlyData(data) {
    monthlyData = getKLineData(data);
}