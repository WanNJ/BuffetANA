/**
 * Created by Accident on 18/05/2017.
 */
let accumulatePaybackChart = echarts.init(document.getElementById('accumulatePaybackChart'));

function loadAccumulatedLineChart(dates, stdData, normalData) {

    let accumulatePaybackOption = {
        title: {
            show: false,
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            },
            formatter: function (params) {
                let res = '';
                res = res + params[0].name;

                for (let i = 0; i < params.length; i++) {
                    let value = Math.round(params[i].value * 100) / 100;
                    res = res + '<br/><span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[i].color + '"></span>' + params[i].seriesName + ': ' + value;
                }
                return res;
            }
        },
        // legend: {
        //     data: ['基准', '不择时','温度高趋反性强', '温度高趋同性强', '温度低趋同性强', '温度低趋反性强']
        // },
        axisPointer: {
            link: {xAxisIndex: 'all'},
            label: {
                backgroundColor: '#777'
            }
        },
        toolbox: {
            right: 100,
            feature: {
                saveAsImage: {},
                restore: {}
            }
        },
        grid: [
            {
                top: '10%',
                height: '75%'
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: dates,
                splitNumber: 20
            }
        ],
        yAxis: [
            {
                type: 'value',
                scale: true,
                splitArea: {
                    show: false
                }
            }
        ],
        dataZoom: [
            {
                type: 'inside',
            },
            {
                show: true,
                realtime: true,
                type: 'slider'
            }
        ],
        series: [
            {
                name: '基准',
                type: 'line',
                data: stdData,
                showSymbol: false,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            width: 1.5
                        }
                    }
                }
            },
            {
                name: '不择时',
                type: 'line',
                data: normalData,
                showSymbol: false,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            width: 1.5
                        }
                    }
                },
            }
            // {
            //     name: '温度高趋同性强',
            //     type: 'line',
            //     data: objData.prices,
            //     showSymbol: false,
            //     itemStyle: {
            //         normal: {
            //             lineStyle: {
            //                 width: 1.5
            //             }
            //         }
            //     },
            // },
            // {
            //     name: '温度低趋同性强',
            //     type: 'line',
            //     data: objData.prices,
            //     showSymbol: false,
            //     itemStyle: {
            //         normal: {
            //             lineStyle: {
            //                 width: 1.5
            //             }
            //         }
            //     },
            // },
            // {
            //     name: '温度低趋反性强',
            //     type: 'line',
            //     data: objData.prices,
            //     showSymbol: false,
            //     itemStyle: {
            //         normal: {
            //             lineStyle: {
            //                 width: 1.5
            //             }
            //         }
            //     },
            // }
        ]
    };
    // 使用刚指定的配置项和数据显示图表
    accumulatePaybackChart.setOption(accumulatePaybackOption);
}

setTimeout(() => {
    window.onresize = function () {
        accumulatePaybackChart.resize();
    }
}, 200);