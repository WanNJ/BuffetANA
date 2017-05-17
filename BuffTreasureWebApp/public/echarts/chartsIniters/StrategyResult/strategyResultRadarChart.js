/**
 * Created by Accident on 18/05/2017.
 */

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
