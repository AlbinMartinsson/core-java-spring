package eu.arrowhead.core.translator.services.translator;

import eu.arrowhead.core.translator.services.translator.common.TranslatorSetup;
import eu.arrowhead.common.exception.ArrowheadException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.arrowhead.core.translator.services.translator.common.TranslatorHubAccess;
import eu.arrowhead.core.translator.services.translator.common.TranslatorDef.EndPoint;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class TranslatorService {

    //=================================================================================================
    // members
    private final Logger logger = LogManager.getLogger(TranslatorService.class);
    private final Map<Integer, TranslatorHub> hubs = new HashMap<>();

    //=================================================================================================
    // methods
    //-------------------------------------------------------------------------------------------------
    public void start() {
        logger.info("Starting TranslatorService");
    }

    //-------------------------------------------------------------------------------------------------
    public TranslatorHubAccess createTranslationHub(TranslatorSetup setup, String outgoingIp) throws ArrowheadException {
        EndPoint consumerEP;
        EndPoint producerEP;
        try {
            consumerEP = new EndPoint(setup.getConsumerName(), setup.getConsumerAddress());
            producerEP = new EndPoint(setup.getProducerName(), setup.getProducerAddress());
        } catch (URISyntaxException | UnknownHostException ex) {
            throw new ArrowheadException(ex.getMessage(), HttpStatus.SC_BAD_REQUEST);
        }

        if (consumerEP.isLocal() || producerEP.isLocal()) {
            throw new ArrowheadException("Not valid ip address, we need absolute address, not relative (as 127.0.0.1 or localhost)", HttpStatus.SC_BAD_REQUEST);
        } else {

            int translatorId = (producerEP.getName() + consumerEP.getName()).hashCode();
            if (!hubs.isEmpty() && hubs.containsKey(translatorId)) {
                logger.info("Hub already exists!");
                TranslatorHub existingHub = hubs.get(translatorId);
                return new TranslatorHubAccess(existingHub.getTranslatorId(), outgoingIp, existingHub.getHubPort());
            } else {
                logger.info("Creating a new Hub: ClientSpoke type:" + consumerEP.getProtocol() + " ServerSpoke type: " + producerEP.getProtocol());
                TranslatorHub hub = new TranslatorHub(translatorId, consumerEP, producerEP);
                hubs.put(translatorId, hub);
                return new TranslatorHubAccess(hub.getTranslatorId(), outgoingIp, hub.getHubPort());
            }
        }
    }

    //-------------------------------------------------------------------------------------------------
    public ArrayList<TranslatorHubAccess> getAllHubs(String localOutgoingIp) {
        ArrayList<TranslatorHubAccess> response = new ArrayList<>();
        hubs.entrySet().forEach((entry) -> {
            response.add(new TranslatorHubAccess(entry.getValue().getTranslatorId(), localOutgoingIp, entry.getValue().getHubPort()));
        });
        return response;
    }

    //-------------------------------------------------------------------------------------------------
    public TranslatorHubAccess getHub(int translatorId, String localOutgoingIp) throws Exception {
        TranslatorHub hub = hubs.get(translatorId);
        if (hub == null) {
            throw new ArrowheadException("Hub not found ", HttpStatus.SC_NOT_FOUND);
        }
        return new TranslatorHubAccess(translatorId, localOutgoingIp, hub.getHubPort());
    }

}
