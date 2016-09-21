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
import java.io.IOException;
import java.util.List;


/**
 * Created by axel on 19/09/16.
 */
public class MovieRecommender {
    private final GenericUserBasedRecommender recommender;

    private int totalReviews;
    private int totalProducts;
    private int totalUsers;

    public MovieRecommender(String pathOfSourceTXT) throws IOException, TasteException {

        DataModel dataModel = new FileDataModel(new File(convertTxtToCSV(pathOfSourceTXT)));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dataModel);
        this.recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
    }

    private String convertTxtToCSV(String pathOfSourceTXT) {

        if (!file_CSV_exist){
            // Lee TXT
//            Convierte
        }else{


        }



        String pathOfSourceCSV;



        return pathOfSourceCSV;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public int getTotalProducts() {
        recommender.getDataModel().getNumItems()

        return totalProducts;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public List<String> getRecommendationsForUser(String user) {
//        recommender.getDataModel().getItemIDs().

        recommender.recommend(Long.parseLong(user), 4);
        return null;
    }
}
