// 基于准备好的dom，初始化echarts实例
let weekly_KLineChart = echarts.init(document.getElementById('weeklyKLineChart'), 'shine');
let weekly_KDJChart = echarts.init(document.getElementById('weekly_KDJChart'), 'shine');
let weekly_MACDChart = echarts.init(document.getElementById('weekly_MACDChart'), 'shine');
let weekly_RSIChart = echarts.init(document.getElementById('weekly_RSIChart'), 'shine');
let weekly_BOLLChart = echarts.init(document.getElementById('weekly_BOLLChart'), 'shine');
let weekly_WRChart = echarts.init(document.getElementById('weekly_WRChart'), 'shine');
let weekly_BIASChart = echarts.init(document.getElementById('weekly_BIASChart'), 'shine');

function loadWeeklyKLineChart(objData) {
    let weekly_KLineChartOption = {
        title: {
            show: false,
            text: '个股',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            },
            position: (pos, params, el, elRect, size) => {
                let obj = {top: 30};
                obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 100;
                return obj;
            },
            formatter: function (params) {
                let res = '';
                let index = params[0].dataIndex;
                let changeRate = weekly_KLineChartOption.series[0].changeRates[index];

                for (let i = 0; i < params.length; i++) {
                    let value = params[i].value;

                    if (params[i].seriesName === '成交量') {
                        if (value !== '-')
                            value = Math.round(params[i].value * 100) / 100;
                        res = res + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[i].color + '"></span>' + params[i].seriesName + ': ' + value;
                        if (i === 0)
                            res += '<br/>';
                    } else if (params[i].seriesName === '日K') {
                        res = res + '月K' + '<br/>开盘价: ' + value[0] + '  收盘价: ' + value[1] + '<br/>最低价: ' + value[2] + ' 最高价: ' + value[3] + '<br/>涨跌幅: ' + Math.round(changeRate * 100) / 100 + '%<br/>';
                    } else {
                        if (value !== '-')
                            value = Math.round(params[i].value * 100) / 100;
                        res = res + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[i].color + '"></span>' + params[i].seriesName + ': ' + value;
                        if (i !== params.length - 1 && params[i].seriesName === 'MA30')
                            res += '<br/>';
                    }

                    if (i !== params.length - 1)
                        res += '<br/>';
                }
                return res;
            }
        },
        legend: {
            data: ['周K', 'MA5', 'MA10', 'MA20', 'MA30'],
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
                height: '55%',
                left: 35,
                right: 0
            },
            {
                top: '72%',
                height: '15%',
                left: 35,
                right: 0
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: objData.categoryData,
                splitNumber: 20
            },
            {
                type: 'category',
                gridIndex: 1,
                data: objData.categoryData,
                axisLabel: {show: false},
            }
        ],
        yAxis: [
            {
                type: 'value',
                scale: true,
                splitArea: {
                    show: false
                }
            },
            {
                type: 'value',
                scale: true,
                gridIndex: 1,
                axisLabel: {show: false},
                axisLine: {show: false},
                axisTick: {show: false},
                splitLine: {show: false}
            }
        ],
        dataZoom: [
            {
                type: 'inside',
                xAxisIndex: [0, 1],
                startValue: objData.categoryData.length - dataZoomLength,
                endValue: objData.categoryData.length - 1
            },
            {
                show: true,
                realtime: true,
                xAxisIndex: [0, 1],
                type: 'slider',
                startValue: objData.categoryData.length - dataZoomLength,
                endValue: objData.categoryData.length - 1
            }
        ],
        series: [
            {
                name: '日K',
                type: 'candlestick',
                data: objData.KLineValue,
                changeRates: objData.changeRates,
            },
            {
                name: 'MA5',
                type: 'line',
                data: calculateMA(5, objData.adjs),
                smooth: true,
                lineStyle: {
                    normal: {opacity: 0.5}
                },
                showSymbol: false
            },
            {
                name: 'MA10',
                type: 'line',
                data: calculateMA(10, objData.adjs),
                smooth: true,
                lineStyle: {
                    normal: {opacity: 0.5}
                },
                showSymbol: false
            },
            {
                name: 'MA20',
                type: 'line',
                data: calculateMA(20, objData.adjs),
                smooth: true,
                lineStyle: {
                    normal: {opacity: 0.5}
                },
                showSymbol: false
            },
            {
                name: 'MA30',
                type: 'line',
                data: calculateMA(30, objData.adjs),
                smooth: true,
                lineStyle: {
                    normal: {opacity: 0.5}
                },
                showSymbol: false
            },
            {
                name: '成交量',
                type: 'bar',
                xAxisIndex: 1,
                yAxisIndex: 1,
                data: objData.volumns,
                turnOverRates: objData.turnOverRates
            }
        ]
    };
    let weekly_KDJChartOption = {
        title: {
            show: false,
            text: 'KDJ',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            },
            position: (pos, params, el, elRect, size) => {
                let obj = {top: 30};
                obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 100;
                return obj;
            },
            formatter: function (params) {
                let res = '';

                for (let i = 0; i < params.length; i++) {
                    let value = params[i].value;

                    if (value !== '-')
                        value = Math.round(params[i].value * 100) / 100;
                    res = res + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[i].color + '"></span>' + params[i].seriesName + ': ' + value;
                    if (i !== 2)
                        res += '<br/>';
                }
                return res;
            }
        },
        legend: {
            data: ['K值', 'D值', 'J值'],
        },
        axisPointer: {
            link: {xAxisIndex: 'all'},
            label: {
                backgroundColor: '#777'
            }
        },
        toolBox: {
            show: false
        },
        grid: [
            {
                top: '25%',
                height: '70%',
                left: 35,
                right: 0
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: objData.categoryData,
                axisLabel: {show: false},
            }
        ],
        yAxis: [
            {
                scale: true,
                splitArea: {
                    show: false
                },
                splitNumber: 2,
                splitLine: {show: false}
            }
        ],
        dataZoom: [
            {
                type: 'inside',
                startValue: objData.categoryData.length - dataZoomLength,
                endValue: objData.categoryData.length - 1
            },
            {
                show: false,
                realtime: true,
                type: 'slider',
                startValue: objData.categoryData.length - dataZoomLength,
                endValue: objData.categoryData.length - 1
            }
        ],
        series: [
            {
                name: 'K值',
                type: 'line',
                data: objData.kIndexes,
                markArea: {
                    silent: true,
                    data: [[{
                        yAxis: 20
                    }, {
                        yAxis: 80
                    }]]
                },
                itemStyle: {
                    normal: {
                        color: 'grey',
                        lineStyle: {
                            width: 1,
                        }
                    }
                },
                showSymbol: false
            },
            {
                name: 'D值',
                type: 'line',
                data: objData.dIndexes,
                itemStyle: {
                    normal: {
                        color: 'orange',
                        lineStyle: {
                            width: 1
                        }
                    }
                },
                showSymbol: false
            },
            {
                name: 'J值',
                type: 'line',
                data: objData.jIndexes,
                itemStyle: {
                    normal: {
                        color: 'purple',
                        lineStyle: {
                            width: 1
                        }
                    }
                },
                showSymbol: false
            },
        ]
    };
    let weekly_MACDChartOption = {
        title: {
            show: false,
            text: 'MACD',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            },
            position: (pos, params, el, elRect, size) => {
                let obj = {top: 30};
                obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 100;
                return obj;
            },
            formatter: function (params) {
                let res = '';

                for (let i = 0; i < params.length; i++) {
                    let value = params[i].value;

                    if (value !== '-')
                        value = Math.round(params[i].value * 100) / 100;
                    res = res + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[i].color + '"></span>' + params[i].seriesName + ': ' + value;
                    if (i !== 2)
                        res += '<br/>';
                }
                return res;
            }
        },
        legend: {
            data: ['MACD', 'DIF', 'DEA'],
        },
        axisPointer: {
            link: {xAxisIndex: 'all'},
            label: {
                backgroundColor: '#777'
            }
        },
        toolBox: {
            show: false
        },
        grid: [
            {
                top: '25%',
                height: '70%',
                left: 35,
                right: 0
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: objData.categoryData,
                axisLabel: {show: false},
                splitNumber: 20
            }
        ],
        yAxis: [
            {
                scale: true,
                splitArea: {
                    show: false
                },
                splitNumber: 2,
                axisLine: {onZero: false},
                axisTick: {show: false},
                splitLine: {show: false},
                axisLabel: {show: true}
            }
        ],
        dataZoom: [
            {
                type: 'inside',
                startValue: objData.categoryData.length - dataZoomLength,
                endValue: objData.categoryData.length - 1
            },
            {
                show: false,
                realtime: true,
                type: 'slider',
                startValue: objData.categoryData.length - dataZoomLength,
                endValue: objData.categoryData.length - 1
            }
        ],
        series: [
            {
                name: 'MACD',
                type: 'bar',
                data: objData.macds,
                itemStyle: {
                    normal: {
                        color: function (params) {
                            let colorList;
                            if (params.data >= 0) {
                                colorList = '#ef232a';
                            } else {
                                colorList = '#14b143';
                            }
                            return colorList;
                        }
                    }
                },
                showSymbol: false
            },
            {
                name: 'DIF',
                type: 'line',
                data: objData.difs,
                showSymbol: false
            },
            {
                name: 'DEA',
                type: 'line',
                data: objData.deas,
                showSymbol: false
            }
        ]
    };
    let weekly_RSIChartOption = {
        title: {
            show: false,
            text: 'RSI',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            },
            position: (pos, params, el, elRect, size) => {
                let obj = {top: 30};
                obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 100;
                return obj;
            },
            formatter: function (params) {
                let res = '';

                for (let i = 0; i < params.length; i++) {
                    let value = params[i].value;

                    if (value !== '-')
                        value = Math.round(params[i].value * 100) / 100;
                    res = res + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[i].color + '"></span>' + params[i].seriesName + ': ' + value;
                    if (i !== 2)
                        res += '<br/>';
                }
                return res;
            }
        },
        legend: {
            data: ['RSI6', 'RSI12', 'RSI24'],
        },
        axisPointer: {
            link: {xAxisIndex: 'all'},
            label: {
                backgroundColor: '#777'
            }
        },
        toolBox: {
            show: false
        },
        grid: [
            {
                top: '25%',
                height: '70%',
                left: 35,
                right: 0
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: objData.categoryData,
                axisLabel: {show: false},
            }
        ],
        yAxis: [
            {
                scale: true,
                splitArea: {
                    show: false
                },
                splitNumber: 2,
                splitLine: {show: false}
            }
        ],
        dataZoom: [
            {
                type: 'inside',
                startValue: objData.categoryData.length - 60,
                endValue: objData.categoryData.length - 1
            },
            {
                show: false,
                realtime: true,
                type: 'slider',
                startValue: objData.categoryData.length - 60,
                endValue: objData.categoryData.length - 1
            }
        ],
        series: [
            {
                name: 'RSI6',
                type: 'line',
                data: objData.rsi6s,
                markArea: {
                    silent: true,
                    data: [[{
                        yAxis: 20
                    }, {
                        yAxis: 80
                    }]]
                },
                itemStyle: {
                    normal: {
                        color: 'grey',
                        lineStyle: {
                            width: 1,
                        }
                    }
                },
                showSymbol: false
            },
            {
                name: 'RSI12',
                type: 'line',
                data: objData.rsi12s,
                itemStyle: {
                    normal: {
                        color: 'orange',
                        lineStyle: {
                            width: 1
                        }
                    }
                },
                showSymbol: false
            },
            {
                name: 'RSI24',
                type: 'line',
                data: objData.rsi24s,
                itemStyle: {
                    normal: {
                        color: 'purple',
                        lineStyle: {
                            width: 1
                        }
                    }
                },
                showSymbol: false
            },
        ]
    };
    let weekly_BOLLChartOption = {
        title: {
            show: false,
            text: 'BOLL',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            },
            position: (pos, params, el, elRect, size) => {
                let obj = {top: 30};
                obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 100;
                return obj;
            },
            formatter: function (params) {
                let res = '';

                for (let i = 0; i < params.length; i++) {
                    let value = params[i].value;

                    if (value !== '-')
                        value = Math.round(params[i].value * 100) / 100;
                    res = res + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[i].color + '"></span>' + params[i].seriesName + ': ' + value;
                    if (i !== 2)
                        res += '<br/>';
                }
                return res;
            }
        },
        legend: {
            data: ['BOLL', 'UPPER', 'LOWER'],
        },
        axisPointer: {
            link: {xAxisIndex: 'all'},
            label: {
                backgroundColor: '#777'
            }
        },
        toolBox: {
            show: false
        },
        grid: [
            {
                top: '25%',
                height: '70%',
                left: 35,
                right: 0
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: objData.categoryData,
                axisLabel: {show: false},
            }
        ],
        yAxis: [
            {
                scale: true,
                splitArea: {
                    show: false
                },
                splitNumber: 2,
                splitLine: {show: false}
            }
        ],
        dataZoom: [
            {
                type: 'inside',
                startValue: objData.categoryData.length - 60,
                endValue: objData.categoryData.length - 1
            },
            {
                show: false,
                realtime: true,
                type: 'slider',
                startValue: objData.categoryData.length - 60,
                endValue: objData.categoryData.length - 1
            }
        ],
        series: [
            {
                name: 'BOLL',
                type: 'line',
                data: objData.Boll,
                itemStyle: {
                    normal: {
                        color: 'grey',
                        lineStyle: {
                            width: 1,
                        }
                    }
                },
                showSymbol: false
            },
            {
                name: 'UPPER',
                type: 'line',
                data: objData.upper,
                itemStyle: {
                    normal: {
                        color: 'orange',
                        lineStyle: {
                            width: 1
                        }
                    }
                },
                showSymbol: false
            },
            {
                name: 'LOWER',
                type: 'line',
                data: objData.lower,
                itemStyle: {
                    normal: {
                        color: 'purple',
                        lineStyle: {
                            width: 1
                        }
                    }
                },
                showSymbol: false
            },
        ]
    };
    let weekly_WRChartOption = {
        title: {
            show: false,
            text: 'WR',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            },
            position: (pos, params, el, elRect, size) => {
                let obj = {top: 30};
                obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 100;
                return obj;
            },
            formatter: function (params) {
                let res = '';

                for (let i = 0; i < params.length; i++) {
                    let value = params[i].value;

                    if (value !== '-')
                        value = Math.round(params[i].value * 100) / 100;
                    res = res + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[i].color + '"></span>' + params[i].seriesName + ': ' + value;
                    if (i !== 1)
                        res += '<br/>';
                }
                return res;
            }
        },
        legend: {
            data: ['WR1', 'WR2'],
        },
        axisPointer: {
            link: {xAxisIndex: 'all'},
            label: {
                backgroundColor: '#777'
            }
        },
        toolBox: {
            show: false
        },
        grid: [
            {
                top: '25%',
                height: '70%',
                left: 35,
                right: 0
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: objData.categoryData,
                axisLabel: {show: false},
            }
        ],
        yAxis: [
            {
                scale: true,
                splitArea: {
                    show: false
                },
                splitNumber: 2,
                splitLine: {show: false}
            }
        ],
        dataZoom: [
            {
                type: 'inside',
                startValue: objData.categoryData.length - 60,
                endValue: objData.categoryData.length - 1
            },
            {
                show: false,
                realtime: true,
                type: 'slider',
                startValue: objData.categoryData.length - 60,
                endValue: objData.categoryData.length - 1
            }
        ],
        series: [
            {
                name: 'WR1',
                type: 'line',
                data: objData.WR1,
                markArea: {
                    silent: true,
                    data: [[{
                        yAxis: 20
                    }, {
                        yAxis: 80
                    }]]
                },
                itemStyle: {
                    normal: {
                        color: 'blue',
                        lineStyle: {
                            width: 1,
                        }
                    }
                },
                showSymbol: false
            },
            {
                name: 'WR2',
                type: 'line',
                data: objData.WR2,
                itemStyle: {
                    normal: {
                        color: '#70FF5D',
                        lineStyle: {
                            width: 1
                        }
                    }
                },
                showSymbol: false
            }
        ]
    };
    let weekly_BIASChartOption = {
        title: {
            show: false,
            text: 'BIAS',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            },
            position: (pos, params, el, elRect, size) => {
                let obj = {top: 30};
                obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 100;
                return obj;
            },
            formatter: function (params) {
                let res = '';

                for (let i = 0; i < params.length; i++) {
                    let value = params[i].value;

                    if (value !== '-')
                        value = Math.round(params[i].value * 100) / 100;
                    res = res + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[i].color + '"></span>' + params[i].seriesName + ': ' + value;
                    if (i !== 2)
                        res += '<br/>';
                }
                return res;
            }
        },
        legend: {
            data: ['BIAS6', 'BIAS12', 'BIAS24'],
        },
        axisPointer: {
            link: {xAxisIndex: 'all'},
            label: {
                backgroundColor: '#777'
            }
        },
        toolBox: {
            show: false
        },
        grid: [
            {
                top: '25%',
                height: '70%',
                left: 35,
                right: 0
            }
        ],
        xAxis: [
            {
                type: 'category',
                data: objData.categoryData,
                axisLabel: {show: false},
            }
        ],
        yAxis: [
            {
                scale: true,
                splitArea: {
                    show: false
                },
                splitNumber: 2,
                splitLine: {show: false}
            }
        ],
        dataZoom: [
            {
                type: 'inside',
                startValue: objData.categoryData.length - 60,
                endValue: objData.categoryData.length - 1
            },
            {
                show: false,
                realtime: true,
                type: 'slider',
                startValue: objData.categoryData.length - 60,
                endValue: objData.categoryData.length - 1
            }
        ],
        series: [
            {
                name: 'BIAS6',
                type: 'line',
                data: objData.BIAS6,
                itemStyle: {
                    normal: {
                        color: 'orange',
                        lineStyle: {
                            width: 1,
                        }
                    }
                },
                showSymbol: false
            },
            {
                name: 'BIAS12',
                type: 'line',
                data: objData.BIAS12,
                itemStyle: {
                    normal: {
                        color: 'blue',
                        lineStyle: {
                            width: 1
                        }
                    }
                },
                showSymbol: false
            },
            {
                name: 'BIAS24',
                type: 'line',
                data: objData.BIAS24,
                itemStyle: {
                    normal: {
                        color: 'red',
                        lineStyle: {
                            width: 1
                        }
                    }
                },
                showSymbol: false
            },
        ]
    };

    // 使用刚指定的配置项和数据显示图表
    weekly_KLineChart.setOption(weekly_KLineChartOption);
    weekly_KDJChart.setOption(weekly_KDJChartOption);
    weekly_MACDChart.setOption(weekly_MACDChartOption);
    weekly_RSIChart.setOption(weekly_RSIChartOption);
    weekly_BOLLChart.setOption(weekly_BOLLChartOption);
    weekly_WRChart.setOption(weekly_WRChartOption);
    weekly_BIASChart.setOption(weekly_BIASChartOption);
}

echarts.connect([weekly_KLineChart, weekly_KDJChart, weekly_MACDChart, weekly_RSIChart, weekly_BOLLChart, weekly_WRChart, weekly_BIASChart]);

setTimeout(() => {
    window.onresize = function () {
        weekly_KLineChart.resize();
        weekly_KDJChart.resize();
        weekly_MACDChart.resize();
        weekly_RSIChart.resize();
        weekly_BOLLChart.resize();
        weekly_WRChart.resize();
        weekly_BIASChart.resize();
    }
}, 200);