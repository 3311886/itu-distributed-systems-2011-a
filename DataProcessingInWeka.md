# Introduction #

### Loading training data ###
Choose **Explorer** in the GUI-Chooser. In the explorer choose **Open File...** and pick the file you want to load.
The attributes and relation should be shown for the data to be valid.

### Filtering training data ###
To clean up the data it is possible to apply various filters by clicking the **Filter** button. It is not completely clear what the filters do, but after choosing a filter you can get a short description if you click in the textbox next to **Choose**. Each filter have several proporties, documentation is available at http://weka.sourceforge.net/doc/

### Cross validation ###
Cross-validation is one of several approaches to estimating how well the model you've just learned from some training data is going to perform on future as-yet-unseen data.

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/weka_crossvalidation.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/weka_crossvalidation.png)

Choose the **Classify** tab and select the **cross validation** radio button (1). Then select a classifier (2) and choose which attribute you want to cross validate by (3). We used BayesNet in handin3, but it is pretty difficult to say which is the best. Result details is shown in the **classifier output** window (4). Finding the correct classifier in pretty much trial and error. Especially as classifiers are nested and each level contain settings.

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/weka_classifier_options.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/weka_classifier_options.png)

### Using test data ###
In this phase data should be collected and preprocessed in the same way as the training data.
Select the **Supplied test set** (1) radio button and press the button **Set...**. Select the attribute you want to classify by (2). Make sure that the same attribute is chosen for the training set (3). Press **Start** (4)

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/weka_test_data.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/weka_test_data.png)

### DataFiles ###
Datafiles can be downloaded here: [weka\_files.zip](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/files/weka_files.zip)

When loading csv files into Weka or generating arff files from csv files make sure you have column names defined at the top, eg.

`action,x,y,z`

`sitting,-4.750,-0.34476,8.04451`

From command line arff files can be generated using the Weka jar.

`java weka.core.converters.CSVLoader filename.csv > filename.arff`

Make sure to either add Weka.jar to the classpath or copy the jar to the folder of the csv file and use the "-cp wekajarfilename.jar" in the command