import pandas as pd
competences = pd.read_csv('train.csv')
X = competences.drop(columns=['Dest', 'Dest1'])
y = competences['Dest1']
events = list(set(competences['Event'].to_numpy()))
s1= 0
s2= 0
from sklearn.model_selection import train_test_split
train_x, test_x, train_y, test_y = train_test_split(X, y, test_size=0.15, random_state=0)
from sklearn.neural_network import MLPClassifier
nn = MLPClassifier(activation='tanh', hidden_layer_sizes=(200, 100), max_iter=1100, solver='lbfgs', learning_rate='adaptive', tol=0.000001).fit(train_x.values, train_y.values)

competences = competences[competences['Dest1'] != 0]
X = competences.drop(columns=['Dest', 'Dest1'])
y = competences['Dest']
train_x1, test_x1, train_y1, test_y1 = train_test_split(X, y, test_size=0.15, random_state=0)
from sklearn.tree import DecisionTreeClassifier
m = DecisionTreeClassifier(criterion = 'entropy', max_features = 'sqrt', max_depth = 14)
m.fit(train_x1.values, train_y1.values)

f = open('text.txt', 'w')
for i in events:
    for j in range (1, 21):
        for k in range (1, 6):
            d = nn.predict([[i, j, k]])
            if (d[0] == 0):
                s1 += 1
                f.write(str(i) + ' ' + str(j) + ' ' + str(k) + ' ' + str(k) + '\n')
            else:
                d = m.predict([[i,j,k]])
                s2 += 1
                f.write(str(i) + ' ' + str(j) + ' ' + str(k) + ' ' + str(d[0]) + '\n')
f.close()