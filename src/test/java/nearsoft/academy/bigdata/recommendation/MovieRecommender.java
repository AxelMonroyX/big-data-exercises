package nearsoft.academy.bigdata.recommendation;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;


/**
 * Created by axel on 19/09/16.
 */
public class MovieRecommender {
    private final GenericUserBasedRecommender recommender;

    private int totalReviews;
    private int totalProducts;
    private int totalUsers;

    public MovieRecommender(String pathOfSourceTXT) throws IOException, TasteException {

        DataModel dataModel = new FileDataModel(new File(createDataModelFromGZ(pathOfSourceTXT)));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dataModel);
        this.recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
    }

    private String createDataModelFromGZ(String pathOfSourceGZ) {
        String pathOfSourceCSV = null;
        String pathOfSourceTXT = "/home/axel/code/big-data-exercises/files/movies.txt";
        if (!file_exist(pathOfSourceGZ)) {
            unGunzipFile(pathOfSourceGZ, pathOfSourceTXT);
            convert_TXT_to_CSV(pathOfSourceTXT);
        } else {


        }




        return pathOfSourceCSV;
    }

    private void convert_TXT_to_CSV(String pathOfSourceTXT) {
    }

    private boolean file_exist(String pathOfSourceGZ) {
        return false;

    }

    private void unGunzipFile(String compressedFile, String decompressedFile) {

        byte[] buffer = new byte[1024];

        try {
            FileInputStream fileIn = new FileInputStream(compressedFile);
            GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);
            FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);
            int bytes_read;
            while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bytes_read);
            }
            gZIPInputStream.close();
            fileOutputStream.close();

            System.out.println("The file was decompressed successfully!");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
