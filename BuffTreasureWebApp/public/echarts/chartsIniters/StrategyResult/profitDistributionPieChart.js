/**
 * Created by Accident on 18/05/2017.
 */
let normal_pieChart = echarts.init(document.getElementById('normal_pieChart'));
let highAndOpp_pieChart = echarts.init(document.getElementById('highAndOpp_pieChart'));
let highAndSame_pieChart = echarts.init(document.getElementById('highAndSame_pieChart'));
let lowAndSame_pieChart = echarts.init(document.getElementById('lowAndSame_pieChart'));
let lowAndOpp_pieChart = echarts.init(document.getElementById('lowAndOpp_pieChart'));

function loadDistributionPieChart(chart, data) {
    let profitDistributionOption = {
        title: {
            show: false,
        },
        tooltip : {
            trigger: 'item',
            formatter: "{b} : {c} ({d}%)"
        },
        toolbox: {
            right: 100,
            feature: {
                saveAsImage: {},
                restore: {}
            }
        },
        series : [
            {
                name:'收益分布',
                type:'pie',
                radius : '75%',
                center: ['50%', '50%'],
                data: data,
                roseType: 'radius',
                label: {
                    normal: {
                        textStyle: {
                            color: 'rgba(255, 255, 255, 0.3)'
                        }
                    }
                },
                labelLine: {
                    normal: {
                        lineStyle: {
                            color: 'rgba(255, 255, 255, 0.3)'
                        },
                        smooth: 0.2,
                        length: 10,
                        length2: 20
                    }
                },
                itemStyle: {
                    normal: {
                        color: '#c23531',
                        shadowBlur: 200,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                },

                animationType: 'scale',
                animationEasing: 'elasticOut'
            }
        ]
    };

    chart.setOption(profitDistributionOption);

    setTimeout(() => {
        window.onresize = function () {
            chart.resize();
        }
    }, 200);
}