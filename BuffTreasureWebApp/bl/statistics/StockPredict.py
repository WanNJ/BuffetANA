import numpy as np
from sklearn import svm
from sklearn import cross_validation
import sys
import pymongo


def connect_mongodb():
    servers = "mongodb://localhost:27017"
    conn = pymongo.MongoClient(servers)
    db = conn.allInfo
    return db


db = connect_mongodb()
stock = db[sys.argv[1]].find({"volume": {"$ne": 0}}, ['close', 'high', 'low', 'open', 'volume', 'turnOverRate'])\
    .sort("date")
stockInfo = []
for i in stock:
    stockInfo.append(i)

if len(stockInfo) > 500:
    stockInfo = stockInfo[-500:]
num = len(stockInfo)

day_feature = 150
feature_num = 6 * day_feature
x = np.zeros((num - day_feature, feature_num + 1))
y = np.zeros((num - day_feature))
for i in range(0, num - day_feature):
    for j in range(0, 150):
        x[i, 6 * j] = stockInfo[i + j]['close']
        x[i, 6 * j + 1] = stockInfo[i + j]['high']
        x[i, 6 * j + 2] = stockInfo[i + j]['low']
        x[i, 6 * j + 3] = stockInfo[i + j]['open']
        x[i, 6 * j + 4] = stockInfo[i + j]['volume']
        x[i, 6 * j + 5] = stockInfo[i + j]['turnOverRate']
    x[i, feature_num] = stockInfo[i + day_feature]['open']
for i in range(0, num - day_feature):
    if stockInfo[i + day_feature]['close'] >= stockInfo[i + day_feature]['open']:
        y[i] = 1
    else:
        y[i] = 0

clf = svm.SVC(kernel='rbf')
x_train, x_test, y_train, y_test = cross_validation.train_test_split(x, y, test_size=0.2)
clf.fit(x_train, y_train)
accuracy = np.mean(y_test == clf.predict(x_test))
pre = []
for i in stockInfo[-150:]:
    pre.append(i['close'])
    pre.append(i['high'])
    pre.append(i['low'])
    pre.append(i['open'])
    pre.append(i['volume'])
    pre.append(i['turnOverRate'])
pre.append(float(sys.argv[2]))
predict = clf.predict([pre])
print(str(accuracy) + ',' + str(int(predict[0])))
