/**
 * Created by Accident on 18/05/2017.
 */
let normal_pieChart = echarts.init(document.getElementById('normal_pieChart'));
let highAndSame_pieChart = echarts.init(document.getElementById('highAndSame_pieChart'));
let highAndOpp_pieChart = echarts.init(document.getElementById('highAndOpp_pieChart'));
let lowAndSame_pieChart = echarts.init(document.getElementById('lowAndSame_pieChart'));
let lowAndOpp_pieChart = echarts.init(document.getElementById('lowAndOpp_pieChart'));

function loadDistributionPieChart(chart, data) {
    let profitDistributionOption = {
        title: {
            show: false,
        },
        color:['#53FF53', '#00EC00','#00BB00','#FF9797', '#FF5151', '#EA0000'],
        tooltip : {
            trigger: 'item',
            formatter: "{b}<br/> 数量 : {c} <br/> 比例 : {d}%"
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
                            color: 'black'
                        }
                    }
                },
                labelLine: {
                    normal: {
                        lineStyle: {
                            color: 'black'
                        },
                        smooth: 0.2,
                        length: 10,
                        length2: 20
                    }
                },

                animationType: 'scale',
                animationEasing: 'elasticOut',
                animationDelay: function (idx) {
                    return Math.random() * 200;
                }
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