import tensorflow as tf

import CNNDataSet
import datetime


mmDate = datetime.datetime.strptime('2010-04-05', '%Y-%m-%d')

mapStep = [2,2,2,3,3,3,4,4,4,4,4]
mapSpeed = [0,500,1000,2000,3000,6000]



def test_CNN_Model_All(code, holdingDays,speedMode,isENV,beginDate=datetime.datetime.strptime('2010-04-05', '%Y-%m-%d')):
    step = mapStep[holdingDays]
    DataSet = CNNDataSet.CNNDataSet(beginDate,holdingDays=holdingDays,step=step,code=code,isENV=isENV)

    def weight_variable(shape):
        initial = tf.truncated_normal(shape, stddev=0.0000001)
        return tf.Variable(initial)


    def bias_variable(shape):
        initial = tf.constant(0.0000001, shape=shape)
        return tf.Variable(initial)

    x = tf.placeholder("float", shape=[None, step*4,step*4])
    y_ = tf.placeholder("float", shape=[None, 10])



    [x_input,y_input] = DataSet.getTrainSet()
    [x_test,y_test] = DataSet.getTestSet()

    x_predict = DataSet.getPredictData()

    def conv2d(x, W, s):
        return tf.nn.conv2d(x, W, strides=[1, s, s, 1], padding='SAME')


    def max_pool_2x2(x):
        return tf.nn.max_pool(x, ksize=[1, 2, 2, 1], strides=[1, 2, 2, 1], padding='SAME')

    W_conv1 = weight_variable([step, step, 1, 32])
    b_conv1 = bias_variable([32])

    x_train = tf.reshape(x, [-1,step*4,step*4,1])

    h_conv1 = tf.nn.relu(conv2d(x_train, W_conv1,step) + b_conv1)
    h_pool1 = max_pool_2x2(h_conv1)

    W_fc1 = weight_variable([2*2 * 32, 300])
    b_fc1 = bias_variable([300])

    h_pool2_flat = tf.reshape(h_pool1, [-1, 2*2*32])
    h_fc1 = tf.nn.relu(tf.matmul(h_pool2_flat, W_fc1) + b_fc1)

    keep_prob = tf.placeholder("float")
    h_fc1_drop = tf.nn.dropout(h_fc1, keep_prob)

    W_fc2 = weight_variable([300, 10])
    b_fc2 = bias_variable([10])

    y_conv=tf.nn.softmax(tf.matmul(h_fc1_drop, W_fc2) + b_fc2)

    cross_entropy = -tf.reduce_sum(y_ * tf.log(tf.clip_by_value(y_conv, 1e-10, 1.0)))
    train_step = tf.train.AdamOptimizer(0.001).minimize(cross_entropy)
    correct_prediction = tf.equal(tf.argmax(y_conv,1), tf.argmax(y_,1))

    sess = tf.InteractiveSession()
    sess.run(tf.global_variables_initializer())

    accuracy = tf.reduce_mean(tf.cast(correct_prediction, "float"))
    sess.run(tf.initialize_all_variables())
    process = []
    for i in range(mapSpeed[speedMode]):
        if i % 50 == 0:
            accp = (accuracy.eval(feed_dict={
                x: x_test, y_: y_test, keep_prob: 1.0}))
            process.append(accp)
        train_step.run(feed_dict={x: x_input, y_: y_input, keep_prob: 0.5})


    acc =(accuracy.eval(feed_dict={
        x: x_test, y_: y_test, keep_prob: 1.0}))

    result = (y_conv[0].eval(feed_dict={
                x: [x_predict], y_: [[0, 0, 0, 0, 0, 0, 0, 0, 0, 1]], keep_prob: 1.0}))

    headList = ['less10', '10-7.5', '7.5-5', '5-2.5', '2.5-0', '0-2.5', '2.5-5', '5-7.5', '7.5-10', 'more10']
    resultJson = {'accuracy': acc, 'process': process}

    for i in range(10):
        resultJson[headList[i]] = result[i]
    return resultJson



def test_CNN_Model_Batch(code, holdingDays,speedMode,isENV,beginDate=datetime.datetime.strptime('2010-04-05', '%Y-%m-%d')):
    step = mapStep[holdingDays]
    DataSet = CNNDataSet.CNNDataSet(beginDate,holdingDays=holdingDays,step=step,code=code,isENV=isENV)
    DataSet.calculteBase(beginDate,step)

    def weight_variable(shape):
        initial = tf.truncated_normal(shape, stddev=0.0000001)
        return tf.Variable(initial)


    def bias_variable(shape):
        initial = tf.constant(0.0000001, shape=shape)
        return tf.Variable(initial)






    x = tf.placeholder("float", shape=[None, step*4,step*4])
    y_ = tf.placeholder("float", shape=[None, 10])

    [x_test,y_test] = DataSet.getTestSet()

    x_predict = DataSet.getPredictData()








    def conv2d(x, W, s):
        return tf.nn.conv2d(x, W, strides=[1, s, s, 1], padding='SAME')


    def max_pool_2x2(x):
        return tf.nn.max_pool(x, ksize=[1, 2, 2, 1], strides=[1, 2, 2, 1], padding='SAME')

    W_conv1 = weight_variable([step, step, 1, 32])
    b_conv1 = bias_variable([32])

    x_train = tf.reshape(x, [-1,step*4,step*4,1])

    h_conv1 = tf.nn.relu(conv2d(x_train, W_conv1,step) + b_conv1)
    h_pool1 = max_pool_2x2(h_conv1)

    W_fc1 = weight_variable([2*2 * 32, 300])
    b_fc1 = bias_variable([300])

    h_pool2_flat = tf.reshape(h_pool1, [-1, 2*2*32])
    h_fc1 = tf.nn.relu(tf.matmul(h_pool2_flat, W_fc1) + b_fc1)

    keep_prob = tf.placeholder("float")
    h_fc1_drop = tf.nn.dropout(h_fc1, keep_prob)

    W_fc2 = weight_variable([300, 10])
    b_fc2 = bias_variable([10])

    y_conv=tf.nn.softmax(tf.matmul(h_fc1_drop, W_fc2) + b_fc2)

    cross_entropy = -tf.reduce_sum(y_ * tf.log(tf.clip_by_value(y_conv, 1e-10, 1.0)))
    train_step = tf.train.AdamOptimizer(0.001).minimize(cross_entropy)
    correct_prediction = tf.equal(tf.argmax(y_conv,1), tf.argmax(y_,1))

    sess = tf.InteractiveSession()
    sess.run(tf.global_variables_initializer())

    accuracy = tf.reduce_mean(tf.cast(correct_prediction, "float"))
    sess.run(tf.initialize_all_variables())
    process = []

    for i in range(mapSpeed[speedMode]):
        if i % 50 == 0:
            accp = (accuracy.eval(feed_dict={
                x: x_test, y_: y_test, keep_prob: 1.0}))
            process.append(accp)

        [x_input,y_input] = DataSet.getNextBatch()
        train_step.run(feed_dict={x: x_input, y_: y_input, keep_prob: 0.5})


    acc =(accuracy.eval(feed_dict={
        x: x_test, y_: y_test, keep_prob: 1.0}))

    result = (y_conv[0].eval(feed_dict={
                x: [x_predict], y_: [[0, 0, 0, 0, 0, 0, 0, 0, 0, 1]], keep_prob: 1.0}))

    headList = ['less10','10-7.5','7.5-5','5-2.5','2.5-0','0-2.5','2.5-5','5-7.5','7.5-10','more10']

    resultJson = {'accuracy': acc,'process':process}

    for i in range(10):
        resultJson[headList[i]] = result[i]
    return resultJson

import sys
code = sys.argv[1]
holdingDays = sys.argv[2]
speedMode = sys.argv[3]
isENV = sys.argv[4]
dateStr = sys.argv[5]
testMode = sys.argv[6]

def test_CNN(code, holdingDays,speedMode,isENV,dateStr,testMode):
    out = None
    if testMode == 'All':
        out = test_CNN_Model_All(code=code,holdingDays=holdingDays,speedMode=speedMode,isENV=(isENV=='True'),beginDate=datetime.datetime.strptime(dateStr, '%Y-%m-%d'))
    else:
        out = test_CNN_Model_Batch(code=code,holdingDays=holdingDays,speedMode=speedMode,isENV=(isENV=='True'),beginDate=datetime.datetime.strptime(dateStr, '%Y-%m-%d'))
    import json
    print(json.dumps(out))

test_CNN(code=code,holdingDays=holdingDays,speedMode=speedMode,isENV=isENV,dateStr=dateStr,testMode=testMode)