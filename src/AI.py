import keras
from keras import models
from keras import layers
import numpy as np
import os

model_dir=''
def creatmodel():
    mo = models.Sequential()
    mo.add(layers.Dense(800, activation='relu', input_shape=(19 * 19,)))
    mo.add(layers.Dense(600, activation='tanh'))
    mo.add(layers.Dense(500, activation='relu'))
    mo.add(layers.Dense(19 * 19, activation='relu'))
    mo.compile(optimizer='rmsprop', loss='categorical_crossentropy', metrics=['accuracy'])
    return  mo;
def getmodel():
    if(os.path.exists(model_dir)):
        mo=models.load_model(model_dir)
        return mo
    mo=creatmodel()
    return mo
def win(qp):
    for i in range(0,18):
        c=0
        for j in range(1,18):
            if(qp[i][j]==qp[i][j-1])&(qp[i][j]!=-1):
                c+=1
            else:
                c=0
            if(c==5):
                return qp[i][j]
        c=0
        for j in range(1,18):
            if(qp[j][i]==qp[j-1][i])&(qp[j][i]!=-1):
                c+=1
            else:
                c=0
            if(c==5):
                return qp[j][i]
    for i in range(0,37):
        x=i+1
        y=1
        if(i>=19):
            x=1
            y=i-19+1
        c=0
        while(x>=0&y>=0&x<19&y<19):
            if(qp[x][y]==qp[x-1][y-1]):
                c+=1
            else:
                c=0
            if(c==5):
                return qp[x][y]
            x+=1
            y+=1
        x = i -1
        y = 1
        if (i >= 19):
            x = 18
            y = i - 19 - 1
        c = 0
        while (x >= 0 & y >= 0 & x < 19 & y < 19):
            if (qp[x][y] == qp[x + 1][y - 1]):
                c += 1
            else:
                c = 0
            if (c == 5):
                return qp[x][y]
            x -= 1
            y += 1




def trainmodel(mo):
    qp=np.zeros((19,19))
    count=0;
    while(not win(qp)):
        for i in range(0,18):
            for j in range(0, 18):
                qp[i][j]=-1
        ans=mo.predict(qp)
        ans=ans.reshape((19,19))
        maxi=0
        maxj=0;
        for i in range(0,18):
            for j in range(0, 18):
                if((ans[i][j]>ans[maxi][maxj])or qp[maxi][maxj]==-1)and qp[i][j]!=-1 :
                    maxi=i
                    maxj=j
        


if __name__ == '__main__':
    mymodel=getmodel()
    mymodel.fit()
    mymodel


    print("hello world")
    pass;