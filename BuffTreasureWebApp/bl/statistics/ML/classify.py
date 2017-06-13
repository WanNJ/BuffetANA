from sklearn.ensemble import RandomForestClassifier



import numpy as np


def getImportance(stockSet,holdingDays):

    re = {}
    predict_Re = np.zeros(3)
    js = 0
    maxAccuracy = 0
    import random
    import time
    def pred1():
        random.seed(time.time())
        RandomForestClassifier(bootstrap=False, class_weight=None, criterion='gini',
                               max_depth=None, max_features=5, max_leaf_nodes=None,
                               min_samples_leaf=1, min_samples_split=2,
                               min_weight_fraction_leaf=None, n_estimators=10, n_jobs=1,
                               oob_score=True, random_state=None, verbose=0,
                               warm_start=None, )
        rf = RandomForestClassifier(max_features=None,random_state=None)

        if(re == {}):
            for feature in  stockSet['feature_names']:
                re[feature]= []


        rf.fit(stockSet['data'][:-100], stockSet['target'][:-100])


        # print(len(stockSet['data']))

        instance = stockSet['data'][-100:-1]
        real = stockSet['target'][-100:-1]


        pre = rf.predict_proba(instance)

        important = rf.feature_importances_

        pre_instance  = stockSet['predict']

        pre_result  =  rf.predict_proba(pre_instance)


        for i in range(3):
            predict_Re[i]+=pre_result[0][i]
        # print(pre_result)


        cnt = 0;
        for i in range(0, 99):
            ind = int(real[i])
            if pre[i][ind] >= pre[i][(ind+1)%3] and pre[i][ind] >=pre[i][(ind+2)%3] :
                cnt += 1

        for i in range(len( stockSet['feature_names'])):
            re[stockSet['feature_names'][i]].append(important[i])
        return cnt/100



    for i in range(30):
        acc = pred1()
        if(maxAccuracy<acc):
             maxAccuracy = acc

    for i in range(3):
        predict_Re[i] = predict_Re[i]/50



    def getAttribute(arr):
        narr = np.array(arr)
        max = np.max(narr)
        min = np.min(narr)
        distance = (max - min)/5
        y = np.zeros(6)
        x = np.zeros(6)
        # s(x,y)
        if(max!=min):
            for t in arr:
                num = int((t-min)/distance)
                y[num]+=1
                x[num]+=t

        big = 0 ;

        mean = np.mean(arr)

        return mean
        # plt.figure()
        # plt.hist(x,y)
        # plt.show()

    param = {}
    for m in stockSet['feature_names']:
        imp = getAttribute(re[m])
        param[m] = imp

    def dict2list(dic):
        keys = dic.keys()
        vals = dic.values()
        lst = [(key, val) for key, val in zip(keys, vals)]
        return lst



    result = sorted(dict2list(param), key=lambda x:x[1], reverse=True)
    return result[:10]



