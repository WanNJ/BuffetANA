// 基于准备好的dom，初始化echarts实例
let timeSharingChart = echarts.init(document.getElementById('timeSharingChart'), 'shine');

function calculateMA(dayCount) {
    let result = [];
    let len = data.adjs.length;
    for (let i = 0; i < len; i++) {
        if (i < dayCount) {
            result.push('-');
            continue;
        }
        let sum = 0;
        for (let j = 0; j < dayCount; j++) {
            sum += data.adjs[i - j];
        }
        result.push(sum / dayCount);
    }
    return result;
}

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
                showSymbol: false
            },
            {
                name: '均价',
                type: 'line',
                data: objData.avgPrices,
                showSymbol: false
            },
            {
                name: '成交量',
                type: 'bar',
                xAxisIndex: 1,
                yAxisIndex: 1,
                data: data.volumns
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