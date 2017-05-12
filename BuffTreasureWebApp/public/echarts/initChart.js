// 基于准备好的dom，初始化echarts实例
let otherChart = echarts.init(document.getElementById('otherChart'), 'shine');
let kLineChart = echarts.init(document.getElementById('kLineChart'), 'shine');

let rawData = [
    ['1991-02-26',
        65.07,
        65.07,
        65.07,
        65.07,
        0.49,
        8336900,
        0.31,
        97.91909530432295,
        91.45661186210381,
        78.53164497766551,
        6.169807912642362,
        7.723215307844846,
        -3.106814790404968],
    ['1991-02-27',
        65.4,
        65.4,
        65.4,
        65.4,
        0.51,
        4344900,
        0.16,
        98.61273020288196,
        93.8419846423632,
        84.3004935213257,
        5.908690696776894,
        7.360310385631256,
        -2.903239377708724],
    ['1991-02-28',
        65.07,
        65.07,
        65.07,
        65.07,
        -0.5,
        2573100,
        0.09,
        95.24145314061553,
        94.30847414178064,
        92.44251614411087,
        5.610443411448941,
        7.010336990794793,
        -2.7997871586917054],
    ['1991-03-01',
        64.74,
        64.74,
        64.74,
        64.74,
        -0.51,
        1440900,
        0.05,
        87.05638110420162,
        91.89110979592095,
        101.56056717935962,
        5.286563476659815,
        6.665582287967799,
        -2.7580376226159675],
    ['1991-03-04',
        64.1,
        64.1,
        64.1,
        64.1,
        -0.99,
        749800,
        0.03,
        68.92024210314193,
        84.23415389832795,
        114.86197748869998,
        4.921484399811817,
        6.316762710336603,
        -2.7905566210495714],
    ['1991-03-05',
        63.36,
        63.36,
        63.36,
        63.36,
        -1.15,
        639600,
        0.02,
        45.946828068761285,
        71.47171195513906,
        122.5214797278946,
        4.520310030203703,
        5.957472174310023,
        -2.874324288212641],
    ['1991-03-06',
        63.04,
        63.04,
        63.04,
        63.04,
        -0.51,
        463100,
        0.02,
        30.631218712507522,
        57.85821420759521,
        112.3122051977706,
        4.1289750109357755,
        5.5917727416351735,
        -2.925595461398796],
    ['1991-03-07',
        62.72,
        62.72,
        62.72,
        62.72,
        -0.51,
        213200,
        0.01,
        20.420812475005015,
        45.37908029673181,
        95.29561594018539,
        3.749808542723727,
        5.223379901852884,
        -2.9471427182583145],
    ['1991-03-08',
        62.41,
        62.41,
        62.41,
        62.41,
        -0.49,
        1433600,
        0.05,
        13.613874983336675,
        34.7906785256001,
        77.14428561012696,
        3.385258648338528,
        4.855755651150013,
        -2.9409940056229704],
    ['1991-03-11',
        61.8,
        61.8,
        61.8,
        61.8,
        -0.98,
        1345300,
        0.05,
        9.075916655557783,
        26.219091235585992,
        60.50544039564241,
        3.0124454986254605,
        4.487093620645103,
        -2.949296244039285],
    ['1991-03-12',
        61.49,
        61.49,
        61.49,
        61.49,
        -0.5,
        3212700,
        0.12,
        6.050611103705188,
        19.49626452495906,
        46.387571367466805,
        2.661276273082457,
        4.121930151132574,
        -2.9213077561002336],
    ['1991-04-03',
        49,
        49,
        49,
        49,
        -20.31,
        5200,
        0,
        4.033740735803459,
        14.342089928573857,
        34.958788314114656,
        1.3594568938327285,
        3.569435499672605,
        -4.419957211679753]
];

let data = splitData(rawData);
/**
 *      日期，           开盘价，    收盘价，    最低价，    最高价，    涨跌幅(已乘100)，    成交量，    换手率(已乘100)，    K   D   J
 * eg:  '2017-05-05'    10.2       11.50      10.10      11.50     1.275               1232       2.23               80  90  70
 * @param rawData
 * @returns {{categoryData: Array, KLineValue: Array, volumns: Array}}
 */
function splitData(rawData) {
    let categoryData = [];
    let values = [];
    let volumns = [];
    let turnOverRates = [];
    let kIndexes = [];
    let dIndexes = [];
    let jIndexes = [];
    let changeRates = [];
    for (let i = 0; i < rawData.length; i++) {
        categoryData.push(rawData[i].splice(0, 1)[0]);
        values.push(rawData[i].splice(0, 4));
        changeRates.push(rawData[i].splice(0, 1)[0]);
        volumns.push(rawData[i].splice(0, 1)[0]);
        kIndexes.push(rawData[i].splice(0, 1)[0]);
        dIndexes.push(rawData[i].splice(0, 1)[0]);
        jIndexes.push(rawData[i].splice(0, 1)[0]);
        turnOverRates.push(rawData[i].splice(0, 1)[0]);
    }
    return {
        categoryData: categoryData,
        KLineValue: values,
        changeRates: changeRates,
        volumns: volumns,
        turnOverRates: turnOverRates,
        kIndexes: kIndexes,
        dIndexes: dIndexes,
        jIndexes: jIndexes
    };
}

function calculateMA(dayCount) {
    let result = [];
    let len = data.KLineValue.length;
    for (let i = 0; i < len; i++) {
        if (i < dayCount) {
            result.push('-');
            continue;
        }
        let sum = 0;
        for (let j = 0; j < dayCount; j++) {
            sum += data.KLineValue[i - j][1];
        }
        result.push(sum / dayCount);
    }
    return result;
}

//TODO ToolTip里显示不了涨跌幅和换手率
kLineChartOption = {
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
            obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 200;
            return obj;
        },
        formatter: function (params) {
            let res = '';
            let index = params[0].dataIndex;
            let turnOverRate = kLineChartOption.series[5].turnOverRates[index];
            let changeRate = kLineChartOption.series[0].changeRates[index];

            for (let i = 0; i < params.length; i++) {
                let value = params[i].value;

                if (params[i].seriesName === '成交量') {
                    if (value !== '-')
                        value = Math.round(params[i].value * 100) / 100;
                    res = res + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[i].color + '"></span>' + params[i].seriesName + ': ' + value;
                    res = res + '<br/>换手率' + ': ' + Math.round(turnOverRate * 100) / 100 + '%';
                    if (i === 0)
                        res += '<br/>';
                } else if (params[i].seriesName === '日K') {
                    res = res + '日K' + '<br/>开盘价: ' + value[0] + '  收盘价: ' + value[1] + '<br/>最低价: ' + value[2] + ' 最高价: ' + value[3] + '<br/>涨跌幅: ' + Math.round(changeRate * 100) / 100 + '%<br/>';
                } else {
                    if (value !== '-')
                        value = Math.round(params[i].value * 100) / 100;
                    res = res + '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + params[i].color + '"></span>' + params[i].seriesName + ': ' + value;
                    if(i !== params.length - 1 && params[i].seriesName === 'MA30')
                        res += '<br/>';
                }

                if (i !== params.length - 1)
                    res += '<br/>';
            }
            return res;
        }
    },
    legend: {
        data: ['日K', 'MA5', 'MA10', 'MA20', 'MA30'],
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
            height: '50%'
        },
        {
            top: '65%',
            height: '20%'
        }
    ],
    xAxis: [
        {
            type: 'category',
            data: data.categoryData,
            scale: true,
            boundaryGap: false,
            axisLine: {onZero: false},
            splitLine: {show: false},
            splitNumber: 20
        },
        {
            type: 'category',
            gridIndex: 1,
            data: data.categoryData,
            scale: true,
            boundaryGap: false,
            axisLine: {onZero: false},
            axisTick: {show: false},
            splitLine: {show: false},
            axisLabel: {show: false},
            splitNumber: 20
        }
    ],
    yAxis: [
        {
            type : 'value',
            scale: true,
            splitArea: {
                show: false
            }
        },
        {
            type : 'value',
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
        },
        {
            show: true,
            realtime: true,
            xAxisIndex: [0, 1],
            type: 'slider',
            startValue: -30,
            endValue: -1
        }
    ],
    series: [
        {
            name: '日K',
            type: 'candlestick',
            data: data.KLineValue,
            changeRates: data.changeRates,
            markPoint: {
                label: {
                    normal: {
                        formatter: function (param) {
                            return param !== null ? Math.round(param.value) : '';
                        }
                    }
                },
                data: [
                    {
                        name: '最高价',
                        type: 'max',
                        valueDim: 'highest'
                    },
                    {
                        name: '最低价',
                        type: 'min',
                        valueDim: 'lowest'
                    }
                ]
            },
        },
        {
            name: 'MA5',
            type: 'line',
            data: calculateMA(5),
            smooth: true,
            lineStyle: {
                normal: {opacity: 0.5}
            }
        },
        {
            name: 'MA10',
            type: 'line',
            data: calculateMA(10),
            smooth: true,
            lineStyle: {
                normal: {opacity: 0.5}
            }
        },
        {
            name: 'MA20',
            type: 'line',
            data: calculateMA(20),
            smooth: true,
            lineStyle: {
                normal: {opacity: 0.5}
            }
        },
        {
            name: 'MA30',
            type: 'line',
            data: calculateMA(30),
            smooth: true,
            lineStyle: {
                normal: {opacity: 0.5}
            }
        },
        {
            name: '成交量',
            type: 'bar',
            xAxisIndex: 1,
            yAxisIndex: 1,
            data: data.volumns,
            turnOverRates: data.turnOverRates
        }
    ]
};

otherChartOption = {
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
            obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 250;
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
            top: '15%',
            height: '70%'
        }
    ],
    xAxis: [
        {
            type: 'category',
            data: data.categoryData,
            scale: true,
            boundaryGap: false,
            axisLine: {onZero: false},
            axisTick: {show: false},
            splitLine: {show: false},
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
            splitLine: {show: false}
        }
    ],
    dataZoom: [
        {
            type: 'inside',
        },
        {
            show: false,
            realtime: true,
            type: 'slider',
            startValue: -30,
            endValue: -1
        }
    ],
    series: [
        {
            name: 'K值',
            type: 'line',
            data: data.kIndexes,
            itemStyle: {
                normal: {
                    color: 'grey',
                    lineStyle: {
                        width: 1,
                    }
                }
            }
        },
        {
            name: 'D值',
            type: 'line',
            data: data.dIndexes,
            itemStyle: {
                normal: {
                    color: 'orange',
                    lineStyle: {
                        width: 1
                    }
                }
            }
        },
        {
            name: 'J值',
            type: 'line',
            data: data.jIndexes,
            itemStyle: {
                normal: {
                    color: 'purple',
                    lineStyle: {
                        width: 1
                    }
                }
            }
        },
    ]
};

// 使用刚指定的配置项和数据显示图表
kLineChart.setOption(kLineChartOption);
otherChart.setOption(otherChartOption);

echarts.connect([kLineChart, otherChart]);

setTimeout(() => {
    window.onresize = function () {
        kLineChart.resize();
        otherChart.resize();
    }
},200);