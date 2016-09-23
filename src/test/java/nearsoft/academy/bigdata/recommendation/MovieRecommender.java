package nearsoft.academy.bigdata.recommendation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;


/**
 * Created by axel on 19/09/16.
 * github.com/AxelMonroyX
 */
class MovieRecommender {
    private static final char NEW_LINE_SEPARATOR = '\n';
    private final GenericUserBasedRecommender recommender;


    private int totalReviews;
    private int totalProducts;

    private BiMap<String, Integer> users = HashBiMap.create();
    private BiMap<String, Integer> products = HashBiMap.create();
    private String pathofFiles = "/home/axel/code/big-data-exercises/files/";

    MovieRecommender(String nameOfFileGZ) throws IOException, TasteException {


        DataModel dataModel = new FileDataModel(new File(pathofFiles, createDataModelFromGZ(nameOfFileGZ)));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dataModel);
        this.recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
    }


    private String createDataModelFromGZ(String nameOfFileGZ) throws IOException {
        createCSV(nameOfFileGZ);

        return nameOfFileGZ + ".csv";
    }



    private void createCSV(String nameOfFileGZ) throws IOException {
        GZIPInputStream in = new GZIPInputStream(new FileInputStream(pathofFiles + nameOfFileGZ));

        Reader decoder = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(decoder);

        FileOutputStream outputStream = new FileOutputStream(pathofFiles + nameOfFileGZ + ".csv");

        String line;
        String product = "";
        StringBuilder finalLine = new StringBuilder();
        int actualUser;
        int actualProduct;
        try {
            while ((line = br.readLine()) != null) {
                if (line.startsWith("product/productId:")) {
                    product = line;
                    line = br.readLine();
                }
                if (line.startsWith("review/userId:")) {

                    if (!users.isEmpty() & users.containsKey(line.substring(15))) {
                        actualUser = users.get(line.substring(15));
                    } else {
                        users.put(line.substring(15), users.size() + 1);
                        actualUser = users.size();
                    }
                    finalLine.append(actualUser).append(",");
                    line = br.readLine();

                }
                if (!product.equals("")) {
                    if (!products.isEmpty() & products.containsKey(product.substring(19))) {
                        actualProduct = products.get(product.substring(19));
                    } else {
                        products.put(product.substring(19), products.size() + 1);
                        actualProduct = products.size();
                    }
                    finalLine.append(actualProduct).append(",");
                    product = "";
                    totalProducts++;
                }

                if (line.startsWith("review/score:")) {
                    finalLine.append(line.substring(14)).append(NEW_LINE_SEPARATOR);
                    totalReviews++;

                }


            }
            outputStream.write(finalLine.toString().getBytes());


        } finally {
            outputStream.close();
            in.close();
            decoder.close();
        }


    }




    int getTotalReviews() {
        return totalReviews;
    }

    int getTotalProducts() {

        totalProducts = products.size();
        return totalProducts;
    }

    int getTotalUsers() {
        return users.size();
    }

    List<String> getRecommendationsForUser(String user) {


        List<String> recommendationsForUser = new ArrayList<String>();
        List<RecommendedItem> listRecommendations = null;
        try {
            listRecommendations = recommender.recommend(users.get(user), 5);
        } catch (TasteException e) {
            e.printStackTrace();
        }
        BiMap<Integer, String> itemsMapInv = this.products.inverse();
        if (listRecommendations != null) {
            for (RecommendedItem recommendation : listRecommendations) {
                int recommendationID = (int) recommendation.getItemID();
                recommendationsForUser.add(itemsMapInv.get(recommendationID));
            }
        }
        return recommendationsForUser;

    }

}
