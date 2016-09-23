package nearsoft.academy.bigdata.recommendation;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.*;
import java.util.List;
import java.util.zip.GZIPInputStream;


/**
 * Created by axel on 19/09/16.
 */
public class MovieRecommender {
    private static final char COMMA_DELIMITER = ',';
    private static final char NEW_LINE_SEPARATOR = '\n';
    private final GenericUserBasedRecommender recommender;

    private int totalReviews;
    private int totalProducts;
    private int totalUsers;
    String pathofFiles = "/home/axel/code/big-data-exercises/files/";

    public MovieRecommender(String nameOfFileGZ) throws IOException, TasteException {


        DataModel dataModel = new FileDataModel(new File(pathofFiles, createDataModelFromGZ(nameOfFileGZ)));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dataModel);
        this.recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
    }


    private String createDataModelFromGZ(String nameOfFileGZ) throws IOException {

        if (!file_CSV_exist(nameOfFileGZ)) {
            createCSV(nameOfFileGZ);
        }
        return nameOfFileGZ + ".csv";
    }

    private boolean file_CSV_exist(String nameOfFileGZ) {
        return false;
    }

    private void createCSV(String nameOfFileGZ) throws IOException {
//        String infile = "file.gzip";
        GZIPInputStream in = new GZIPInputStream(new FileInputStream(pathofFiles + nameOfFileGZ));

        Reader decoder = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(decoder);

        FileOutputStream outputStream = new FileOutputStream(pathofFiles + nameOfFileGZ + ".csv");

        int contador = 0;
        String line;
        String product = "";
        String finalline = "";
        int allCase = 0;
        while ( (line = br.readLine()) != null) {
            System.out.println(line);
            if (line.startsWith("product/productId:")) {
                product = line;

                allCase++;
                line=br.readLine();
            }
            if (line.startsWith("review/userId:")) {
                finalline += line.substring(15);
                finalline += ("" + COMMA_DELIMITER);
                allCase++;
                line=br.readLine();

            }
            if (!product.equals("")) {

                finalline += product.substring(19);
                finalline += ("" + COMMA_DELIMITER);
                product="";
            }
//            outputStream.write(("" + COMMA_DELIMITER).getBytes());

            if (line.startsWith("review/score:")) {
                finalline += line.substring(13);
                allCase++;
                line=br.readLine();
                finalline+=NEW_LINE_SEPARATOR;
             outputStream.write(finalline.getBytes());
            }


            contador++;
            System.out.println(contador);
            if (contador==800){
                break;
            }


        }

        outputStream.close();

    }


    private boolean file_exist(String pathOfSourceGZ) {
        return true;

    }


    public int getTotalReviews() {
        return totalReviews;
    }

    public int getTotalProducts() {
        try {
            recommender.getDataModel().getNumItems();
        } catch (TasteException e) {
            e.printStackTrace();
        }

        return totalProducts;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public List<String> getRecommendationsForUser(String user) {
//        recommender.getDataModel().getItemIDs().

        try {
            recommender.recommend(Long.parseLong(user), 4);
        } catch (TasteException e) {
            e.printStackTrace();
        }
        return null;
    }

}
