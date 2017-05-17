/**
 * Created by Accident on 18/05/2017.
 */
let accumulatePaybackChart = echarts.init(document.getElementById('accumulatePaybackChart'));

function loadTimeSharingChart(objData) {

    let accumulatePaybackOption = {
        title: {
            show: false,
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            }
        },
        legend: {
            data: ['不择时','温度高趋反性强', '温度高趋同性强', '温度低趋同性强', '温度低趋反性强']
        },
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
                data: objData.categoryData,
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
                name: '不择时',
                type: 'line',
                data: objData.prices,
                showSymbol: false,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            width: 1.5
                        }
                    }
                },
            },
            {
                name: '温度高趋反性强',
                type: 'line',
                data: objData.prices,
                showSymbol: false,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            width: 1.5
                        }
                    }
                },
            },
            {
                name: '温度高趋同性强',
                type: 'line',
                data: objData.prices,
                showSymbol: false,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            width: 1.5
                        }
                    }
                },
            },
            {
                name: '温度低趋同性强',
                type: 'line',
                data: objData.prices,
                showSymbol: false,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            width: 1.5
                        }
                    }
                },
            },
            {
                name: '温度低趋反性强',
                type: 'line',
                data: objData.prices,
                showSymbol: false,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            width: 1.5
                        }
                    }
                },
            },
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