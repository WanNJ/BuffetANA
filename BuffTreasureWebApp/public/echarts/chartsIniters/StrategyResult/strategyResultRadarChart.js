/**
 * Created by Accident on 18/05/2017.
 */

// {                                    // 策略评估的雷达图数据
//     "profitAbility" : Number,       // 盈利能力：策略的盈亏比(回测期间总利润除以总亏损)越大，该项分值越高；
//     "stability" : Number,           // 稳定性：策略的波动越小，该项分值越高
//     "chooseStockAbility" : Number,  // 选股能力：策略的成功率越大，该项分值越高
//     "absoluteProfit" : Number,      // 绝对收益：策略的年化收益率越大，该项分值越高
//     "antiRiskAbility" : Number,     // 抗风险能力：策略的回撤越小，该项分值越高；
//     "strategyScore" : Number        // 策略总得分，上面5项得分之和
// }

let strategyResultRadarChart = echarts.init(document.getElementById('strategyEstimateRadarChart'));

function loadStrategyResultRadarChart(objData) {
    let strategyResultRadarOption = {
        title: {
            left: 'center',
            text: '策略总得分: ' + objData.strategyScore,

        },
        tooltip: {

        },
        radar: {
            indicator: [
                { name: '盈利能力', max: 25},
                { name: '稳定性', max: 25},
                { name: '选股能力', max: 25},
                { name: '绝对收益', max: 25},
                { name: '抗风险能力', max: 25}
            ]
        },
        series: [{
            name: '各项指标',
            type: 'radar',
            data : [
                {
                    value : [objData.profitAbility, objData.stability, objData.chooseStockAbility, objData.absoluteProfit, objData.antiRiskAbility],
                    name : '策略得分'
                }
            ]
        }]
    };

    strategyResultRadarChart.setOption(strategyResultRadarOption);
}

setTimeout(() => {
    window.onresize = function () {
        strategyResultRadarChart.resize();
    }
}, 200);
