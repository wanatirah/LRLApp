package com.example.Training;

import org.datavec.api.records.reader.SequenceRecordReader;
import org.datavec.api.records.reader.impl.csv.CSVSequenceRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.core.storage.StatsStorage;
import org.deeplearning4j.datasets.datavec.SequenceRecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.WorkspaceMode;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.model.stats.StatsListener;
import org.deeplearning4j.ui.model.storage.InMemoryStatsStorage;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;

import static java.util.Calendar.PM;

public class ModelTraining {

    private static int numSkipLines = 0;
    private static int batchSize = 1;
    private static double learningRate = 0.01;
    private static int epoch =2000;

    public static void main(String[] args) throws IOException, InterruptedException {

        File baseDir = new ClassPathResource("Data").getFile();
        File featureDir = new File(baseDir, "features");
        File labelDir = new File(baseDir, "labels");

        SequenceRecordReader trainFeatures = new CSVSequenceRecordReader(numSkipLines,",");
        trainFeatures.initialize(new FileSplit(featureDir));
        SequenceRecordReader trainLabels = new CSVSequenceRecordReader(numSkipLines, ",");
        trainLabels.initialize(new FileSplit(labelDir));

        DataSetIterator train = new SequenceRecordReaderDataSetIterator(trainFeatures, trainLabels, batchSize,
                1, true, SequenceRecordReaderDataSetIterator.AlignmentMode.ALIGN_END);

        int numInput = train.inputColumns();
        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .trainingWorkspaceMode(WorkspaceMode.NONE)
                .inferenceWorkspaceMode(WorkspaceMode.NONE)
                .seed(12345)
                .weightInit(WeightInit.XAVIER)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(learningRate))
                .list()
                .layer(0, new LSTM.Builder()
                        .nIn(numInput)
                        .nOut(50)
                        .activation(Activation.TANH)
                        .build())
                .layer(1, new RnnOutputLayer.Builder()
                        .nIn(50)
                        .nOut(1)
                        .lossFunction(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .build())
                .build();

        StatsStorage storage = new InMemoryStatsStorage();
        UIServer server = UIServer.getInstance();
        server.attach(storage);

        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();
        model.setListeners(new StatsListener(storage, 10));

        for (int i=0; i<epoch; i++){
            System.out.println("EPOCH: " + i);
            model.fit(train);
            System.out.println("LOSS: " + model.evaluateRegression(train).averageMeanSquaredError());
        }

        INDArray testInput1 = Nd4j.create(new double[][][] {{
                {   14,   4,   2022, 12, 59, 40, PM}}});
        System.out.println(model.output(testInput1));

    }
}
