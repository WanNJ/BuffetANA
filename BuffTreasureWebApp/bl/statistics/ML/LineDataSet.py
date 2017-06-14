import pymongo
import numpy as np
import classify
import datetime


def connect_mongodb():
    servers = "mongodb://localhost:27017"
    conn = pymongo.MongoClient(servers)
    db = conn.latestInfo
    return db


propre = ['MA5','MA10','MA15','MA30','MA5_before','MA10_before',
          'MA15_before','MA30_before','MA5_after','MA10_after','MA15_after'
         ,'MA30_after','K','D','J','DIF','DEA','MACD','DIF_before','DEA_before'
         ,'MACD_before','DIF_after','DEA_after','MACD_after','RSI6','RSI12'
         , 'RSI24','BIAS6','BIAS12','BIAS24','Boll','upper','lower','Boll_before'
         ,'upper_before','lower_before','Boll_after','upper_after','lower_after',
             'WR2']

def pre_deal_Line_Data(code,holdingDays):
    db = connect_mongodb()

    all_data = []
    for item in db[code].find({"volume": {"$ne": 0}}):
        item.pop('_id')
        all_data.append(item)


    # 找到前复权的基准
    before_index_ratio = 1
    before_index_price = 0
    for i in range(len(all_data)):
        if all_data[i]["volume"] != 0:
            before_index_ratio = all_data[i]["beforeAdjClose"]
            before_index_price = all_data[i]["close"]
            break

    all_data = list(reversed(all_data))

    # 均线所需要的临时数据
    MA5 = []
    MA10 = []
    MA15 = []
    MA30 = []

    MA5_before = []
    MA10_before = []
    MA15_before = []
    MA30_before = []

    MA5_after = []
    MA10_after = []
    MA15_after = []
    MA30_after = []

    # 计算KDJ所需要的临时数据
    beforeK = 50  # 前日K值
    beforeD = 50  # 前日D值
    periodHigh = []
    periodLow = []

    # 计算MACD所需要的临时数据
    beforeEMA12 = 0  # 昨日的12日指数平均值
    beforeEMA26 = 0  # 昨日的26日指数平均值
    beforeDEA = 0    # 昨日的九日DIF平滑移动平均值

    beforeEMA12_before_adj = 0    # 昨日的12日指数平均值
    beforeEMA26_before_adj = 0    # 昨日的26日指数平均值
    beforeDEA_before_adj = 0      # 昨日的九日DIF平滑移动平均值

    beforeEMA12_after_adj = 0     # 昨日的12日指数平均值
    beforeEMA26_after_adj = 0     # 昨日的26日指数平均值
    beforeDEA_after_adj = 0       # 昨日的九日DIF平滑移动平均值

    # 计算RSI所需要的临时数据
    changePrice6 = []
    changePrice12 = []
    changePrice24 = []

    # 计算乖离率BIAS所需要的临时数据
    MA6 = []    # 6日移动平均线
    MA12 = []   # 12日移动平均线
    MA24 = []   # 24日移动平均线


    # 计算布林线Boll所需要的临时数据
    MA20 = []
    MA20_before_adj = []
    MA20_after_adj = []

    # 计算威廉指标WR所需要的临时数据
    high_period_10 = []
    low_period_10 = []
    high_period_6 = []
    low_period_6 = []

    all_day_data = {}

    for data in all_data:
        all_day_data[data["date"]] = {}
        all_day_data[data["date"]]["open"] = data["open"]
        all_day_data[data["date"]]["close"] = data["close"]
        all_day_data[data["date"]]["low"] = data["low"]
        all_day_data[data["date"]]["high"] = data["high"]
        one_day_beforeAdjClose = data["beforeAdjClose"] / before_index_ratio * before_index_price
        all_day_data[data["date"]]["beforeAdjOpen"] = data["open"] * one_day_beforeAdjClose / data["close"]
        all_day_data[data["date"]]["beforeAdjClose"] = one_day_beforeAdjClose
        all_day_data[data["date"]]["beforeAdjLow"] = data["low"] * one_day_beforeAdjClose / data["close"]
        all_day_data[data["date"]]["beforeAdjLow"] = data["high"] * one_day_beforeAdjClose / data["close"]
        all_day_data[data["date"]]["afterAdjOpen"] = data["open"] * data["afterAdjClose"] / data["close"]
        all_day_data[data["date"]]["afterAdjClose"] = data["afterAdjClose"]
        all_day_data[data["date"]]["afterAdjLow"] = data["low"] * data["afterAdjClose"] / data["close"]
        all_day_data[data["date"]]["afterAdjLow"] = data["high"] * data["afterAdjClose"] / data["close"]
        all_day_data[data["date"]]["changeRate"] = data["changeRate"]
        all_day_data[data["date"]]["volume"] = data["volume"]
        all_day_data[data["date"]]["turnOverRate"] = data["turnOverRate"]

        MA5.append(data["close"])
        MA10.append(data["close"])
        MA15.append(data["close"])
        MA30.append(data["close"])
        if len(MA5) > 5:
            del MA5[0]
        if len(MA10) > 10:
            del MA10[0]
        if len(MA15) > 15:
            del MA15[0]
        if len(MA30) > 30:
            del MA30[0]
        all_day_data[data["date"]]["MA5"] = np.average(MA5)
        all_day_data[data["date"]]["MA10"] = np.average(MA10)
        all_day_data[data["date"]]["MA15"] = np.average(MA15)
        all_day_data[data["date"]]["MA30"] = np.average(MA30)

        MA5_before.append(one_day_beforeAdjClose)
        MA10_before.append(one_day_beforeAdjClose)
        MA15_before.append(one_day_beforeAdjClose)
        MA30_before.append(one_day_beforeAdjClose)
        if len(MA5_before) > 5:
            del MA5_before[0]
        if len(MA10_before) > 10:
            del MA10_before[0]
        if len(MA15_before) > 15:
            del MA15_before[0]
        if len(MA30_before) > 30:
            del MA30_before[0]
        all_day_data[data["date"]]["MA5_before"] = np.average(MA5_before)
        all_day_data[data["date"]]["MA10_before"] = np.average(MA10_before)
        all_day_data[data["date"]]["MA15_before"] = np.average(MA15_before)
        all_day_data[data["date"]]["MA30_before"] = np.average(MA30_before)

        MA5_after.append(data["afterAdjClose"])
        MA10_after.append(data["afterAdjClose"])
        MA15_after.append(data["afterAdjClose"])
        MA30_after.append(data["afterAdjClose"])
        if len(MA5_after) > 5:
            del MA5_after[0]
        if len(MA10_after) > 10:
            del MA10_after[0]
        if len(MA15_after) > 15:
            del MA15_after[0]
        if len(MA30_after) > 30:
            del MA30_after[0]
        all_day_data[data["date"]]["MA5_after"] = np.average(MA5_after)
        all_day_data[data["date"]]["MA10_after"] = np.average(MA10_after)
        all_day_data[data["date"]]["MA15_after"] = np.average(MA15_after)
        all_day_data[data["date"]]["MA30_after"] = np.average(MA30_after)

        '''
        计算当日KDJ
        '''
        # 找出9日内的最高和最低价，不足九日的周期，则有几天算几天
        periodHigh.append(data["high"])
        periodLow.append(data["low"])
        if len(periodHigh) > 9 and len(periodLow) > 9:
            del periodHigh[0]
            del periodLow[0]
        high = max(periodHigh)
        low = min(periodLow)
        # 以9日为周期计算RSV
        rsv = 0
        if high != low:
            rsv = (data["close"] - low) / (high - low) * 100
        # K、D的平滑因子分别取1/3和2/3
        k = 2 / 3 * beforeK + 1 / 3 * rsv
        d = 2 / 3 * beforeD + 1 / 3 * k
        j = 3 * k - 2 * d
        beforeK = k
        beforeD = d
        all_day_data[data["date"]]["K"] = k
        all_day_data[data["date"]]["D"] = d
        all_day_data[data["date"]]["J"] = j

        '''
        计算当日MACD
        '''
        # 由于每日行情震荡波动之大小不同，并不适合以每日之收盘价来计算移动平均值，
        # 于是有需求指数（DemandIndex）之产生，用需求指数代表每日的收盘指数
        # 不复权
        DI = (data["close"] * 2 + data["high"] + data["low"]) / 4
        EMA12 = 2 / 13 * DI + 11 / 13 * beforeEMA12
        EMA26 = 2 / 27 * DI + 25 / 27 * beforeEMA26
        DIF = EMA12 - EMA26
        DEA = DIF * 0.2 + beforeDEA * 0.8
        MACD = 2 * (DIF - DEA)
        beforeEMA12 = EMA12
        beforeEMA26 = EMA26
        beforeDEA = DEA
        all_day_data[data["date"]]["DIF"] = DIF
        all_day_data[data["date"]]["DEA"] = DEA
        all_day_data[data["date"]]["MACD"] = MACD

        # 前复权
        high_before = data["high"] * one_day_beforeAdjClose / data["close"]
        low_before = data["low"] * one_day_beforeAdjClose / data["close"]
        DI = (one_day_beforeAdjClose * 2 + high_before + low_before) / 4
        EMA12 = 2 / 13 * DI + 11 / 13 * beforeEMA12_before_adj
        EMA26 = 2 / 27 * DI + 25 / 27 * beforeEMA26_before_adj
        DIF = EMA12 - EMA26
        DEA = DIF * 0.2 + beforeDEA_before_adj * 0.8
        MACD = 2 * (DIF - DEA)
        beforeEMA12_before_adj = EMA12
        beforeEMA26_before_adj = EMA26
        beforeDEA_before_adj = DEA
        all_day_data[data["date"]]["DIF_before"] = DIF
        all_day_data[data["date"]]["DEA_before"] = DEA
        all_day_data[data["date"]]["MACD_before"] = MACD

        # 后复权
        high_after = data["high"] * data["afterAdjClose"] / data["close"]
        low_after = data["low"] * data["afterAdjClose"] / data["close"]
        DI = (data["afterAdjClose"] * 2 + high_after + low_after) / 4
        EMA12 = 2 / 13 * DI + 11 / 13 * beforeEMA12_after_adj
        EMA26 = 2 / 27 * DI + 25 / 27 * beforeEMA26_after_adj
        DIF = EMA12 - EMA26
        DEA = DIF * 0.2 + beforeDEA_after_adj * 0.8
        MACD = 2 * (DIF - DEA)
        beforeEMA12_after_adj = EMA12
        beforeEMA26_after_adj = EMA26
        beforeDEA_after_adj = DEA
        all_day_data[data["date"]]["DIF_after"] = DIF
        all_day_data[data["date"]]["DEA_after"] = DEA
        all_day_data[data["date"]]["MACD_after"] = MACD

        '''
        计算当日RSI
        '''
        changePrice6.append(data["changePrice"])
        changePrice12.append(data["changePrice"])
        changePrice24.append(data["changePrice"])
        if len(changePrice6) > 6:
            del changePrice6[0]
        if len(changePrice12) > 12:
            del changePrice12[0]
        if len(changePrice24) > 24:
            del changePrice24[0]
        upAverage6 = 0
        for price in filter(lambda x: x >= 0, changePrice6):
            upAverage6 += price
        upAverage6 /= 6
        downAverage6 = 0
        for price in filter(lambda x: x <= 0, changePrice6):
            downAverage6 -= price
        downAverage6 /= 6
        upAverage12 = 0
        for price in filter(lambda x: x >= 0, changePrice12):
            upAverage12 += price
        upAverage12 /= 12
        downAverage12 = 0
        for price in filter(lambda x: x <= 0, changePrice12):
            downAverage12 -= price
        downAverage12 /= 12
        upAverage24 = 0
        for price in filter(lambda x: x >= 0, changePrice24):
            upAverage24 += price
        upAverage24 /= 24
        downAverage24 = 0
        for price in filter(lambda x: x <= 0, changePrice24):
            downAverage24 -= price
        downAverage24 /= 24
        RSI6 = 0
        RSI12 = 0
        RSI24 = 0
        if (upAverage6 + downAverage6) != 0:
            RSI6 = upAverage6 / (upAverage6 + downAverage6) * 100
        if (upAverage12 + downAverage12) != 0:
            RSI12 = upAverage12 / (upAverage12 + downAverage12) * 100
        if (upAverage24 + downAverage24) != 0:
            RSI24 = upAverage24 / (upAverage24 + downAverage24) * 100
        all_day_data[data["date"]]["RSI6"] = RSI6
        all_day_data[data["date"]]["RSI12"] = RSI12
        all_day_data[data["date"]]["RSI24"] = RSI24

        '''
        计算当日乖离率BIAS，固定为6、12、24日的乖离率
        '''
        MA6.append(data["close"])
        MA12.append(data["close"])
        MA24.append(data["close"])
        if len(MA6) > 6:
            del MA6[0]
        if len(MA12) > 12:
            del MA12[0]
        if len(MA24) > 24:
            del MA24[0]
        Ave6 = np.average(MA6)
        Ave12 = np.average(MA12)
        Ave24 = np.average(MA24)
        BIAS6 = (data["close"] - Ave6) / Ave6
        BIAS12 = (data["close"] - Ave12) / Ave12
        BIAS24 = (data["close"] - Ave24) / Ave24
        all_day_data[data["date"]]["BIAS6"] = BIAS6
        all_day_data[data["date"]]["BIAS12"] = BIAS12
        all_day_data[data["date"]]["BIAS24"] = BIAS24

        '''
        计算当日布林线Boll
        '''
        # 不复权
        MA20.append(data["close"])
        if len(MA20) > 20:
            del MA20[0]
        MD = np.std(MA20)
        Boll = 0
        if len(MA20) > 1:
            Boll = np.average(MA20[0:-1])
        else:
            Boll = np.average(MA20)
        upper = Boll + 2 * MD
        lower = Boll - 2 * MD
        all_day_data[data["date"]]["Boll"] = Boll
        all_day_data[data["date"]]["upper"] = upper
        all_day_data[data["date"]]["lower"] = lower

        # 前复权
        MA20_before_adj.append(one_day_beforeAdjClose)
        if len(MA20_before_adj) > 20:
            del MA20_before_adj[0]
        MD = np.std(MA20_before_adj)
        Boll = 0
        if len(MA20_before_adj) > 1:
            Boll = np.average(MA20_before_adj[0:-1])
        else:
            Boll = np.average(MA20_before_adj)
        upper = Boll + 2 * MD
        lower = Boll - 2 * MD
        all_day_data[data["date"]]["Boll_before"] = Boll
        all_day_data[data["date"]]["upper_before"] = upper
        all_day_data[data["date"]]["lower_before"] = lower

        # 后复权
        MA20_after_adj.append(data["afterAdjClose"])
        if len(MA20_after_adj) > 20:
            del MA20_after_adj[0]
        MD = np.std(MA20_after_adj)
        Boll = 0
        if len(MA20_after_adj) > 1:
            Boll = np.average(MA20_after_adj[0:-1])
        else:
            Boll = np.average(MA20_after_adj)
        upper = Boll + 2 * MD
        lower = Boll - 2 * MD
        all_day_data[data["date"]]["Boll_after"] = Boll
        all_day_data[data["date"]]["upper_after"] = upper
        all_day_data[data["date"]]["lower_after"] = lower

        '''
        计算当日威廉指标WR，WR1固定为10天买卖强弱指标，WR2固定为6天买卖强弱指标
        '''
        high_period_10.append(data["high"])
        low_period_10.append(data["low"])
        high_period_6.append(data["high"])
        low_period_6.append(data["low"])
        if len(high_period_10) > 10:
            del high_period_10[0]
        if len(low_period_10) > 10:
            del low_period_10[0]
        if len(high_period_6) > 6:
            del high_period_6[0]
        if len(low_period_6) > 6:
            del low_period_6[0]
        high_10 = max(high_period_10)
        low_10 = max(low_period_10)
        high_6 = max(high_period_6)
        low_6 = max(low_period_6)
        WR1 = 0
        WR2 = 0
        if (high_10 - low_10) != 0:
            WR1 = 100 * (high_10 - data["close"]) / (high_10 - low_10)
        if (high_6 - low_6) != 0:
            WR2 = 100 * (high_6 - data["close"]) / (high_6 - low_6)
        all_day_data[data["date"]]["WR1"] = WR1
        all_day_data[data["date"]]["WR2"] = WR2



    begin_learn_date = datetime.datetime.strptime('2006-04-05', '%Y-%m-%d')

    temp_data = {}

    for o in all_day_data:
        if(o-begin_learn_date).days>=0:
            temp_data[o] = all_day_data[o]
    all_day_data = temp_data


    temp_data = []
    for o in all_data:
        if(o['date']-begin_learn_date).days>=0:
            temp_data.append({'date':o['date']})

    all_data = temp_data
    dataSet = {}
    dataSet['feature_names'] = propre

    keys = list(all_day_data.keys())
    dataSet['target'] = np.zeros(len(keys) - 1)
    dd = np.zeros([len(keys) - 1, len(propre)])
    # print(keys)
    for i in range(len(keys)-holdingDays-1):
        date = all_data[i]['date']
        nextDate = all_data[i+holdingDays]['date']
        # print(date)
        for j in range(len(propre)):
            dd[i][j] = all_day_data[date][propre[j]]
            #dd[i][j]= 0
        # print(all_day_data[keys[i+1]]['close'])
        # print(all_day_data[date]['close'])
        # print(all_day_data[nextDate]['close'],all_day_data[date]['close'])
        if((all_day_data[nextDate]['close'] - all_day_data[date]['close'])/ all_day_data[date]['close'])>0.025 :
            dataSet['target'][i] = 0;
        elif ((all_day_data[nextDate]['close'] - all_day_data[date]['close'])/ all_day_data[date]['close'])<-0.025:
            dataSet['target'][i] = 1;
        else:
            dataSet['target'][i] = 2

        # print(dataSet['target'][i])
    dataSet['data'] = dd

    ind = len(keys)-1
    dataSet['predict'] = np.zeros(len(propre))
    date = keys[ind]
    for j in range(len(propre)):
        dataSet['predict'][j] = all_day_data[date][propre[j]]

    imp = classify.getImportance(dataSet,holdingDays)
    # print(imp)







    NNDataSet = {}
    NNDataSet['importance'] = imp;
    NNDataSet['target'] = np.zeros([len(keys)-holdingDays-1,3])
    NNhead = []

    for t in imp:
        NNhead.append(t[0])
    # print(NNhead)
    all_day_data = normalization(all_day_data,NNhead )
    NNDataSet['predict']  = np.zeros([1,10])
    NNDataSet['data'] = np.zeros([len(NNDataSet['target']),10])

    for i in range(len(keys) - holdingDays - 1):
        date = all_data[i]['date']

        NNDataSet['target'][i,int(dataSet['target'][i])]  =1
        for k in range(len(NNhead)):
            NNDataSet['data'][i,k] = all_day_data[date][NNhead[k]]

        date = all_data[-1]['date']
    for k in range(len(NNhead)):
        NNDataSet['predict'][0, k] = all_day_data[date][NNhead[k]]

    # print(NNDataSet['target'])
    return NNDataSet

# 所有的数据都在all_day_data中形如：
# {
#     date: {'open': 10.12,
#            'close': 10.1,
#            'low': 10.04,
#            'high': 10.14,
#            'beforeAdjOpen': 6.8646759138275515,
#            'beforeAdjClose': 6.851109360638168,
#            'beforeAdjLow': 6.878242467016934,
#            'afterAdjOpen': 782.991413861386,
#            'afterAdjClose': 781.444,
#            'afterAdjLow': 784.5388277227723,
#            'changeRate': -0.5906,
#            'volume': 51987541,
#            'turnOverRate': 0.5285,
#            'MA5': 10.172000000000001, 5日均线
#            'MA10': 10.165999999999999, 10日均线
#            'MA15': 10.196, 15日均线
#            'MA30': 10.273, 30日均线
#            'MA5_before': 6.8999542142746026, 前复权5日均线
#            'MA10_before': 6.8958877978775064, 前复权10日均线
#            'MA15_before': 6.9162401281023618, 前复权15日均线
#            'MA30_before': 6.9684739413063879, 前复权30日均线
#            'MA5_after': 787.01529999999991,   后复权5日均线
#            'MA10_after': 786.55147999999986,  后复权10日均线
#            'MA15_after': 788.87288000000001,  后复权15日均线
#            'MA30_after': 794.83072333333325, 后复权30日均线
#            'K': 36.1675855563116,
#            'D': 38.240363348712386,
#            'J': 32.02202997151004,
#            'DIF': -0.06126080367847386,
#            'DEA': -0.0548959758089854,
#            'MACD': -0.012729655738976928,
#            'DIF_before': -0.03886203252038989,
#            'DEA_before': -0.033278354567104984,
#            'MACD_before': -0.01116735590656981,
#            'DIF_after': -4.43264087558407,
#            'DEA_after': -3.795761932233446,
#            'MACD_after': -1.2737578867012482,
#            'RSI6': 46.666666666666664,
#            'RSI12': 39.047619047619044,
#            'RSI24': 44.44444444444445,
#            'BIAS6': -0.0062315513283042884,
#            'BIAS12': -0.0073710073710073019,
#            'BIAS24': -0.018027141989062197,
#            'Boll': 10.246315789473684,
#            'upper': 10.468747801558276,
#            'lower': 10.023883777389091,
#            'Boll_before': 6.9503729815107267,
#            'upper_before': 7.1012617655659644,
#            'lower_before': 6.7994841974554889,
#            'Boll_after': 792.76611052631574,
#            'upper_after': 809.97663157170825,
#            'lower_after': 775.55558948092323,
#            'WR1': 135.29411764705912,
#             'WR2': 146.1538461538469}
# }

def normalization(data,head):
    nor = {}
    js = 0
    for key in list(head):
        nor[key] = []
    for key in data:
        value = data[key]
        for key in list(head):
            nor[key].append(value[key])
        js += 1

    for key in data:
        value = data[key]
        for key in list(head):
                value[key] = (value[key] - np.mean(nor[key])) / np.std(nor[key])
    return data