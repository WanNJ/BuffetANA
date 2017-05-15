// 基于准备好的dom，初始化echarts实例
let daily_KLineChart = echarts.init(document.getElementById('dailyKLineChart'), 'shine');
let daily_KDJChart = echarts.init(document.getElementById('daily_KDJChart'), 'shine');
let daily_MACDChart = echarts.init(document.getElementById('daily_MACDChart'), 'shine');
let daily_RSIChart = echarts.init(document.getElementById('daily_RSIChart'), 'shine');

let daily_rawData = [
    ['2017-03-29',
        9.13,
        9.11,
        9.09,
        9.15,
        -0.11,
        60114000,
        0.36,
        11.488811791795413,
        16.208770670265437,
        25.648688427205485,
        -0.06920844446638519,
        -0.03601808254391087,
        -0.06638072384494864,
        9.11,
        19.047619047619047,
        21.052631578947363,
        29.591836734693878],
    ['2017-03-30',
        9.12,
        9.08,
        9.06,
        9.12,
        -0.33,
        68728500,
        0.41,
        10.32587452786355,
        14.247805289464807,
        22.09166681266732,
        -0.0759072009240036,
        -0.043995906219929416,
        -0.06382258940814838,
        9.08,
        25,
        19.999999999999996,
        28.999999999999996],
    ['2017-03-31',
        9.08,
        9.17,
        9.08,
        9.18,
        0.99,
        63312100,
        0.37,
        25.217249685242336,
        17.904286754723984,
        3.278360893687278,
        -0.07510528987599585,
        -0.0502177829511427,
        -0.0497750138497063,
        9.17,
        42.85714285714285,
        26.15384615384615,
        37.25490196078431],
    ['2017-04-05',
        9.16,
        9.21,
        9.15,
        9.22,
        0.44,
        49915000,
        0.29,
        44.58927756793944,
        26.799283692462467,
        -8.78070405849148,
        -0.06983193746087935,
        -0.05414061385309003,
        -0.031382647215578624,
        9.21,
        54.166666666666664,
        26.15384615384615,
        36.63366336633663],
    ['2017-04-06',
        9.2,
        9.2,
        9.17,
        9.22,
        -0.11,
        43439100,
        0.26,
        55.652110971218704,
        36.41689278538121,
        -2.0535435862937845,
        -0.06490458799079057,
        -0.056293408680630146,
        -0.017222358620320857,
        9.2,
        65,
        37.77777777777778,
        35.64356435643564],
    ['2017-04-07',
        9.19,
        9.2,
        9.17,
        9.22,
        0,
        51484400,
        0.3,
        63.02733324007154,
        45.28703960361132,
        9.80645233069086,
        -0.06030447557985852,
        -0.057095622060475824,
        -0.006417707038765388,
        9.2,
        72.22222222222223,
        43.58974358974359,
        37.89473684210526],
    ['2017-04-10',
        9.2,
        9.18,
        9.17,
        9.21,
        -0.22,
        40134300,
        0.24,
        67.01822216004751,
        52.530767122423384,
        23.555857047175124,
        -0.057010319410839116,
        -0.05707856153054849,
        0.000136484239418741,
        9.18,
        68.42105263157895,
        42.49999999999999,
        38.297872340425535],
    ['2017-04-11',
        9.17,
        9.15,
        9.09,
        9.19,
        -0.33,
        61243700,
        0.36,
        63.42881477336496,
        56.163449672737244,
        41.632719471481806,
        -0.05697062076661297,
        -0.05705697337776139,
        0.00017270522229684282,
        9.15,
        68.42105263157895,
        48.57142857142858,
        33.69565217391304],
    ['2017-04-12',
        9.16,
        9.12,
        9.1,
        9.17,
        -0.33,
        45533600,
        0.27,
        54.78587651557636,
        55.70425862035028,
        57.54102282989811,
        -0.05768629139273962,
        -0.05718283698075704,
        -0.0010069088239651608,
        9.12,
        30.76923076923077,
        38.235294117647065,
        32.63157894736842],
    ['2017-04-13',
        9.11,
        9.12,
        9.1,
        9.14,
        0,
        35744200,
        0.21,
        49.023917677050626,
        53.47747830591706,
        62.38459956364993,
        -0.058187898961442386,
        -0.057383849376894114,
        -0.001608099169096544,
        9.12,
        0,
        39.3939393939394,
        33.69565217391304],
    ['2017-04-14',
        9.11,
        9.08,
        9.06,
        9.12,
        -0.44,
        49050000,
        0.29,
        36.849278451366985,
        47.93474502106703,
        70.10567816046711,
        -0.0607098091766769,
        -0.05804904133685067,
        -0.0053215356796524615,
        9.08,
        0,
        40.625,
        33.69565217391303],
    ['2017-04-17',
        9.08,
        9.1,
        9.05,
        9.11,
        0.22,
        53189200,
        0.31,
        34.3701072028719,
        43.41319908166865,
        61.49938283926214,
        -0.061594954682325564,
        -0.05875822400594565,
        -0.005673461352759826,
        9.1,
        14.285714285714285,
        46.875,
        33.69565217391303],
    ['2017-04-18',
        9.09,
        9.05,
        9.05,
        9.1,
        -0.55,
        33537600,
        0.2,
        22.9134048019146,
        36.579934321750635,
        63.9129933614227,
        -0.06378024013908856,
        -0.059762627232574236,
        -0.008035225813028651,
        9.05,
        11.76470588235294,
        41.666666666666664,
        29.03225806451612],
    ['2017-04-19',
        9.03,
        8.91,
        8.9,
        9.04,
        -1.55,
        79966800,
        0.47,
        16.317269867943043,
        29.825712837148103,
        56.84259877555822,
        -0.0745375978030598,
        -0.06271762134667136,
        -0.023639952912776874,
        8.91,
        7.142857142857142,
        31.914893617021274,
        25.233644859813076],
    ['2017-04-20',
        8.9,
        8.92,
        8.89,
        8.94,
        0.11,
        43763000,
        0.26,
        14.00317991196196,
        24.55153519541939,
        45.648245762334255,
        -0.08391116750784633,
        -0.06695633057890635,
        -0.03390967385787996,
        8.92,
        11.538461538461538,
        17.948717948717945,
        23.07692307692307],
    ['2017-04-21',
        8.92,
        8.97,
        8.9,
        8.99,
        0.56,
        32540800,
        0.19,
        18.22434216353023,
        22.442470851456335,
        30.878728227308542,
        -0.08710799656421209,
        -0.0709866637759675,
        -0.032242665576489166,
        8.97,
        25.806451612903224,
        20,
        23.809523809523803],
    ['2017-04-24',
        8.97,
        8.93,
        8.89,
        8.98,
        -0.45,
        39499500,
        0.23,
        16.911466204258158,
        20.598802635723608,
        27.97347549865451,
        -0.09061425298656012,
        -0.07491218161808604,
        -0.031404142736948165,
        8.93,
        25.806451612903224,
        18.6046511627907,
        28.4090909090909],
    ['2017-04-25',
        8.93,
        9,
        8.93,
        9.01,
        0.78,
        37793300,
        0.22,
        25.940977469505363,
        22.379527580317525,
        15.256627801941846,
        -0.08814064473641636,
        -0.0775578742417521,
        -0.02116554098932852,
        9,
        36.11111111111111,
        30.00000000000001,
        35.95505617977528],
    ['2017-04-26',
        9,
        8.99,
        8.96,
        9.01,
        -0.11,
        38214700,
        0.23,
        31.78673860285868,
        25.515264587831243,
        12.972316557776374,
        -0.08499875058332407,
        -0.0790460495100665,
        -0.011905402146515143,
        8.99,
        40.625,
        30.612244897959183,
        35.95505617977528],
    ['2017-04-27',
        8.97,
        8.97,
        8.91,
        8.98,
        -0.22,
        38739200,
        0.23,
        33.31237118978464,
        28.11430012181571,
        17.718157985877838,
        -0.08396166826300089,
        -0.08002917326065337,
        -0.007864990004695038,
        8.97,
        65,
        31.250000000000007,
        38.55421686746988],
    ['2017-04-28',
        8.96,
        8.99,
        8.92,
        8.99,
        0.22,
        28644600,
        0.17,
        38.08126333287231,
        31.436621192167905,
        18.147336910759094,
        -0.08099573005462268,
        -0.08022248461944724,
        -0.0015464908703508862,
        8.99,
        66.66666666666666,
        36.17021276595745,
        37.03703703703704]];

let daily_data = splitData(daily_rawData);

function splitData(daily_rawData) {
    let categoryData = [];
    let values = [];
    let changeRates = [];
    let volumns = [];
    let turnOverRates = [];
    let kIndexes = [];
    let dIndexes = [];
    let jIndexes = [];
    let difs = [];
    let deas = [];
    let macds = [];
    let adjs = [];
    let rsi6s = [];
    let rsi12s = [];
    let rsi24s = [];
    for (let i = 0; i < daily_rawData.length; i++) {
        categoryData.push(daily_rawData[i].splice(0, 1)[0]);
        values.push(daily_rawData[i].splice(0, 4));
        changeRates.push(daily_rawData[i].splice(0, 1)[0]);
        volumns.push(daily_rawData[i].splice(0, 1)[0]);
        kIndexes.push(daily_rawData[i].splice(0, 1)[0]);
        dIndexes.push(daily_rawData[i].splice(0, 1)[0]);
        jIndexes.push(daily_rawData[i].splice(0, 1)[0]);
        turnOverRates.push(daily_rawData[i].splice(0, 1)[0]);
        difs.push(daily_rawData[i].splice(0, 1)[0]);
        deas.push(daily_rawData[i].splice(0, 1)[0]);
        macds.push(daily_rawData[i].splice(0, 1)[0]);
        adjs.push(daily_rawData[i].splice(0, 1)[0]);
        rsi6s.push(daily_rawData[i].splice(0, 1)[0]);
        rsi12s.push(daily_rawData[i].splice(0, 1)[0]);
        rsi24s.push(daily_rawData[i].splice(0, 1)[0]);
    }
    return {
        categoryData: categoryData,
        KLineValue: values,
        changeRates: changeRates,
        volumns: volumns,
        turnOverRates: turnOverRates,
        kIndexes: kIndexes,
        dIndexes: dIndexes,
        jIndexes: jIndexes,
        difs: difs,
        deas: deas,
        macds: macds,
        adjs: adjs,
        rsi6s: rsi6s,
        rsi12s: rsi12s,
        rsi24s: rsi24s
    };
}

function calculateMA(dayCount) {
    let result = [];
    let len = daily_data.adjs.length;
    for (let i = 0; i < len; i++) {
        if (i < dayCount) {
            result.push('-');
            continue;
        }
        let sum = 0;
        for (let j = 0; j < dayCount; j++) {
            sum += daily_data.adjs[i - j];
        }
        result.push(sum / dayCount);
    }
    return result;
}

daily_KLineChartOption = {
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
            let turnOverRate = daily_KLineChartOption.series[5].turnOverRates[index];
            let changeRate = daily_KLineChartOption.series[0].changeRates[index];

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
            height: '55%'
        },
        {
            top: '72%',
            height: '15%'
        }
    ],
    xAxis: [
        {
            type: 'category',
            data: daily_data.categoryData,
            splitNumber: 20
        },
        {
            type: 'category',
            gridIndex: 1,
            data: daily_data.categoryData,
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
            data: daily_data.KLineValue,
            changeRates: daily_data.changeRates,
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
            data: daily_data.volumns,
            turnOverRates: daily_data.turnOverRates
        }
    ]
};

daily_KDJChartOption = {
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
            top: '25%',
            height: '70%'
        }
    ],
    xAxis: [
        {
            type: 'category',
            data: daily_data.categoryData,
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
            data: daily_data.kIndexes,
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
            data: daily_data.dIndexes,
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
            data: daily_data.jIndexes,
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

daily_MACDChartOption = {
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
            height: '70%'
        }
    ],
    xAxis: [
        {
            type: 'category',
            data: daily_data.categoryData,
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
            name: 'MACD',
            type: 'bar',
            data: daily_data.macds,
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
            }
        },
        {
            name: 'DIF',
            type: 'line',
            data: daily_data.difs
        },
        {
            name: 'DEA',
            type: 'line',
            data: daily_data.deas
        }
    ]
};

daily_RSIChartOption = {
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
            height: '70%'
        }
    ],
    xAxis: [
        {
            type: 'category',
            data: daily_data.categoryData,
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
            name: 'RSI6',
            type: 'line',
            data: daily_data.rsi6s,
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
            name: 'RSI12',
            type: 'line',
            data: daily_data.rsi12s,
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
            name: 'RSI24',
            type: 'line',
            data: daily_data.rsi24s,
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

function loadDailyKLineChart() {
    // 使用刚指定的配置项和数据显示图表
    daily_KLineChart.setOption(daily_KLineChartOption);
    daily_KDJChart.setOption(daily_KDJChartOption);
    daily_MACDChart.setOption(daily_MACDChartOption);
    daily_RSIChart.setOption(daily_RSIChartOption);

    echarts.connect([daily_KLineChart, daily_KDJChart, daily_MACDChart, daily_RSIChart]);

    setTimeout(() => {
        window.onresize = function () {
            daily_KLineChart.resize();
            daily_KDJChart.resize();
            daily_MACDChart.resize();
            daily_RSIChart.resize();
        }
    }, 200);
}

loadDailyKLineChart();