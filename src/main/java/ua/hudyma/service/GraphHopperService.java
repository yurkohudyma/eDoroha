package ua.hudyma.service;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GraphHopperService {
    @Value("${graphhopper.api.key}")
    private String key;
    private final GraphHopper hopper;

    public GraphHopperService() {
        hopper = new GraphHopper();
        hopper.setOSMFile("gh/ukraine-latest.osm.pbf");
        hopper.setGraphHopperLocation("gh/graphFolderPath");
        var carProfile = new Profile("car")
                .setVehicle("car")
                .setWeighting("custom");
        hopper.setProfiles(carProfile);
        hopper.importOrLoad();
    }
}
