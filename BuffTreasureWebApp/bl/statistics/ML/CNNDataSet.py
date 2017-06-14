import datetime
import pymongo
import numpy as np
import copy




label0 = [1,0,0,0,0,0,0,0,0,0]
label1 = [0,1,0,0,0,0,0,0,0,0]
label2 = [0,0,1,0,0,0,0,0,0,0]
label3 = [0,0,0,1,0,0,0,0,0,0]
label4 = [0,0,0,0,1,0,0,0,0,0]
label5 = [0,0,0,0,0,1,0,0,0,0]
label6 = [0,0,0,0,0,0,1,0,0,0]
label7 = [0,0,0,0,0,0,0,1,0,0]
label8 = [0,0,0,0,0,0,0,0,1,0]
label9 = [0,0,0,0,0,0,0,0,0,1]

labels = [label0, label1, label2, label3, label4, label5, label6, label7, label8, label9]



def connect_mongodb():
    servers = "mongodb://localhost:27017"
    conn = pymongo.MongoClient(servers)
    db = conn.latestInfo
    return db




class CNNDataSet(object):
    stock = object
    themom = object
    code = None
    proCode = ['date', 'low', 'open', 'close', 'high', 'afterAdjClose', 'beforeAdjClose', 'changeRate',
               'changePrice','volume', 'marketValue', 'turnOverRate','floatMarketValue']
    proENV = ['date','temperature', 'lastLimitUp', 'lastLimitDown', 'moneyEffect']
    holdingDays = 10;

    db = connect_mongodb()
    collectionCode = None
    collectionENV = db['thermometer']

    stockIndex = 0
    envIndex = 0
    beginDate = None
    endDate = None
    step =4
    isENV  =False

    def __init__(self, beginDate, code ,holdingDays = 10, step = 4,isENV = False):
        self.holdingDays = holdingDays
        self.code = code
        self.collectionCode = self.db[code]
        self.step = step
        self.stock = self._getNormalizationData(beginDate,step)
        self.themom = self._getNormalizationENV(beginDate,step)
        self.endDate = self.stock[-1]['date']

        self.beginDate = beginDate
        self.isENV = isENV
        self.x_train = []
        self.y_train = []
        self.js = 0



    def getRangeDataSet(self,beginDate, endDate,step):
        x_train = []
        y_train = []
        length = (endDate - beginDate).days

        for i in range(0,length):
            date = beginDate+datetime.timedelta(i)
            [x,y] = self.getSingleDataSet(date)
            if x is not None:
                x_train.append(x)
                y_train.append(y)

        return [x_train,y_train]

    def calculteBase(self,beginDate,step):
        x_train = []
        y_train = []
        date = self.endDate + datetime.timedelta(-250)
        length = (date - beginDate).days

        for i in range(0, length):
            date = beginDate + datetime.timedelta(i)
            [x, y] = self.getSingleDataSet(date)
            if x is not None:
                x_train.append(x)
                y_train.append(y)

        self.x_train = x_train
        self.y_train = y_train

    def getTrainSet(self):
        date = self.endDate + datetime.timedelta(-250)
        return self.getRangeDataSet(self.beginDate, date, self.step)

    def getTestSet(self):
        date1 = self.endDate + datetime.timedelta(-200)
        date2 = self.endDate + datetime.timedelta(-50)
        return self.getRangeDataSet(date1, date2, self.step)

    def getPredictData(self):
        self.stockIndex = len(self.stock)-1



        data = []
        last = self.stockIndex - self.step * self.step

        for i in range(self.stockIndex, last, -1):
            temp = copy.deepcopy(self.stock[i])
            temp.pop('date')
            data.append(temp)

        while len(self.themom) > self.envIndex and (self.themom[self.envIndex]['date'] - self.endDate).days < 0:
            self.envIndex += 1


        env = []
        lastE = self.envIndex-1 - self.step * self.step
        for i in range(self.envIndex-1, lastE, -1):
            temp = copy.deepcopy(self.themom[i])
            temp.pop('date')
            env.append(temp)

        return self.getDataGraphByDate(data,env,self.step)

    def getNextBatch(self):
        x_batch = []
        y_batch = []
        first = self.js
        end = self.js+100
        if end >= len(self.x_train):
            for i in range(first,len(self.x_train)):
                x_batch.append(self.x_train[i])
                y_batch.append(self.y_train[i])

            self.js = 0
            return [x_batch,y_batch]
        else:
            for i in range(first, end):
                x_batch.append(self.x_train[i])
                y_batch.append(self.y_train[i])
            self.js = end
            return [x_batch, y_batch]




    def getSingleDataSet(self,date):
        while((self.stock[self.stockIndex]['date']-date).days<0):
            self.stockIndex += 1

        if date!=self.stock[self.stockIndex]['date']:
            return [None,None]

        data = []
        last = self.stockIndex-self.step*self.step


        for i in range(self.stockIndex,last,-1):
            temp = copy.deepcopy(self.stock[i])
            temp.pop('date')
            data.append(temp)

        while len(self.themom) < self.envIndex and (self.themom[self.envIndex]['date'] - date).days < 0:
            self.envIndex += 1

        buy = self.stock[self.stockIndex-1]['close']
        sell = self.stock[self.stockIndex-1+self.holdingDays]['close']

        change = 100 * (sell - buy) / buy
        ll = int((change + 10) / 2.5) + 1
        if ll < 0:
           ll = 0
        if ll > 9:
           ll = 9
        label = labels[ll]



        env = []
        lastE = self.envIndex - self.step * self.step
        for i in range(self.envIndex, lastE, -1):
            temp = copy.deepcopy(self.themom[i])
            temp.pop('date')
            env.append(temp)



        return [self.getDataGraphByDate(data,env,self.step),label]

    def _getNormalizationData(self, beginDate, step):
        data = []
        beginDate = beginDate+datetime.timedelta(-step*step*10)
        cc = self.collectionCode.find({'volume': {'$gt': 0}, 'date': {'$gte': beginDate}}, self.proCode).sort(
            'date',
            pymongo.ASCENDING)
        for o in cc:
            o.pop('_id')
            data.append(o)

        return self.normalization(data)

    def _getNormalizationENV(self,beginDate, step):
        data = []
        beginDate = beginDate + datetime.timedelta(-step * step*10)
        cc = self.collectionENV.find({'date': {'$gte': beginDate}}, self.proENV).sort('date',
                                                                                             pymongo.ASCENDING)
        for o in cc:
            o.pop('_id')
            data.append(o)

        return self.normalization(data)

    #
    def getDataGraphByDate(self,data, environment, step):
        graph = np.zeros([step * 4, step * 4])

        hash = 0

        if len(environment) < step * step:
            return None

        for d in data:
            headList = self.proCode[1:]
            for i in range(len(headList)):

                [row, col] = self.hashMap(hash=hash, i=i, step=step)
                graph[row][col] = d[headList[i]]
            hash += 1

        if self.isENV:
            hash = 0
            for e in environment:
                headListE = self.proENV[1:]
                for i in range(len(headListE)):
                    [row, col] = self.hashMap(hash=hash, i=i + len(self.proCode) - 1, step=step)
                    graph[row][col] = e[headListE[i]]
                hash += 1

        return graph

    def hashMap(self,hash, i, step):
        mock = int(i / 4)
        index = i % 4
        row = int(mock / 2) * step * 2
        col = (mock % 2) * 2 * step
        row += int(index / 2) * step
        col += (index % 2) * step
        row += int(hash / step)
        col += (hash % step)
        return [row, col]

    def normalization(self,data):
        nor = {}
        temp = data[0]
        js = 0
        for key in list(temp.keys()):
            nor[key] = []
        for o in data:
            for key in list(o.keys()):
                nor[key].append(o[key])
            js += 1

        for o in data:
            for key in list(o.keys()):
                if key != 'date':
                    o[key] = (o[key] - np.mean(nor[key])) / np.std(nor[key])
        return data
