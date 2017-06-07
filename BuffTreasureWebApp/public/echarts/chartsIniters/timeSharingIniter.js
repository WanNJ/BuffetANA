// 基于准备好的dom，初始化echarts实例
let timeSharingChart = echarts.init(document.getElementById('timeSharingChart'), 'shine');

function loadTimeSharingChart(objData) {

    let timeSharingOption = {
        title: {
            show: false,
            text: '分时图',
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
            }
        },
        legend: {
            data: ['价格', '均价'],
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
            },
            {
                show: true,
                realtime: true,
                xAxisIndex: [0, 1],
                type: 'slider'
            }
        ],
        series: [
            {
                name: '价格',
                type: 'line',
                data: objData.prices,
                showSymbol: false,
                itemStyle: {
                    normal: {
                        color: '#337ab7',
                        lineStyle: {
                            width: 1.5
                        }
                    }
                },
            },
            {
                name: '均价',
                type: 'line',
                data: objData.avgPrices,
                showSymbol: false,
                itemStyle: {
                    normal: {
                        color: 'grey',
                        lineStyle: {
                            width: 1
                        }
                    }
                },
            },
            {
                name: '成交量',
                type: 'bar',
                xAxisIndex: 1,
                yAxisIndex: 1,
                data: objData.volumns
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表
    timeSharingChart.setOption(timeSharingOption);

    setTimeout(() => {
        window.onresize = function () {
            timeSharingChart.resize();
        }
    }, 200);
}