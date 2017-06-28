import datetime
import os
import pymongo


def connect_mongodb():
    servers = "mongodb://localhost:27017"
    conn = pymongo.MongoClient(servers)
    # print(conn.database_names())
    db = conn.latestInfo
    return db


db = connect_mongodb()
directory_path = '/Users/slow_time/Desktop/AllDataBack/fuquan/'
directory_path2 = '/Users/slow_time/Desktop/AllDataBack/toNow/'
file_names = os.listdir(directory_path)
for filename in file_names:
    if not filename.startswith('.'):
        with open(directory_path + filename, 'r', encoding='gbk') as f1, open(directory_path2 + filename, 'r', encoding='gbk') as f2:
            days_with_adj = f1.readlines()
            days_without_adj = f2.readlines()
            count_adj = 1
            count_without = 1
            length_adj = len(days_with_adj)
            length_without = len(days_without_adj)
            while count_adj < length_adj and count_without < length_without:
                date_adj = datetime.datetime.strptime(days_with_adj[count_adj].split(',')[0], '%Y-%m-%d')
                date_without = datetime.datetime.strptime(days_without_adj[count_without].split(',')[0], '%Y-%m-%d')
                if date_adj == date_without:
                    # Date, qianfuquan, houfuquan
                    stock_adj = days_with_adj[count_adj].split(',')
                    # 日期, 股票代码, 名称, 收盘价, 最高价, 最低价, 开盘价, 前收盘, 涨跌额, 涨跌幅, 换手率, 成交量, 成交金额, 总市值, 流通市值, 成交笔数
                    stock_without = days_without_adj[count_without].split(',')
                    if filename.startswith('60') or filename.startswith('900'):
                        market = 'ss'
                    else:
                        market = 'sz'
                    if int(stock_without[11]) == 0:
                        stockInfo = {
                            "date": date_adj,
                            "open": 0.0,
                            "high": 0.0,
                            "low": 0.0,
                            "close": 0.0,
                            "volume": 0.0,
                            "beforeAdjClose": 0.0,
                            "afterAdjClose": 0.0,
                            "code": filename[0:6],
                            "name": stock_without[2],
                            "market": market,
                            "beforeClose": 0.0,
                            "changePrice": 0.0,
                            "changeRate": 0.0,
                            "turnOverRate": 0.0,
                            "marketValue": float(stock_without[13]),
                            "floatMarketValue": float(stock_without[14])
                        }
                        count_adj += 1
                        count_without += 1
                    else:
                        try:
                            stockInfo = {
                                "date": date_adj,
                                "open": float(stock_without[6]),
                                "high": float(stock_without[4]),
                                "low": float(stock_without[5]),
                                "close": float(stock_without[3]),
                                "volume": int(stock_without[11]),
                                "beforeAdjClose": float(stock_adj[1]),
                                "afterAdjClose": float(stock_adj[2]),
                                "code": filename[0:6],
                                "name": stock_without[2],
                                "market": market,
                                "beforeClose": float(stock_without[7]),
                                "changePrice": float(stock_without[8]),
                                "changeRate": float(stock_without[9]),
                                "turnOverRate": float(stock_without[10]),
                                "marketValue": float(stock_without[13]),
                                "floatMarketValue": float(stock_without[14])
                            }
                            count_adj += 1
                            count_without += 1
                        except Exception as e:
                            count_adj += 1
                            count_without += 1
                            continue
                    # print(stockInfo)
                    print(filename[0:6])
                    collection1 = db[filename[0:6]]
                    collection2 = db[stock_adj[0]]
                    collection1.insert(stockInfo)
                    collection2.insert(stockInfo)
                elif date_adj > date_without:
                    count_adj += 1
                else:
                    count_without += 1

