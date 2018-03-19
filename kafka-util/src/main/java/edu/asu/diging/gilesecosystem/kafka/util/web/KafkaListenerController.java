package edu.asu.diging.gilesecosystem.kafka.util.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.gilesecosystem.kafka.util.service.IKafkaListenerManager;

/**
 * Controller to start and stop Kafka listeners.
 * 
 * The following three mappings are provided:
 * 
 * <ul>
 *  <li><code>/admin/kafka/listeners</code>
 *      <ul>
 *          <li>provides model attribute <code>listenerStatus</code> that is 
 *              true or false depending on the state of the Kafka listeners</li>
 *          <li>expects a Tiles definition <code>admin/kafka/listeners</code></li>
 *      </ul>
 *  </li>
 *  <li><code>/admin/kafka/listeners/start</code>
 *      <ul>
 *          <li>starts Kafka listeners</li>
 *          <li>redirects to <code>/admin/kafka/listeners</code> when done</li>
 *      </ul>
 *  </li>
 *  <li><code>/admin/kafka/listeners/stop</code>
 *      <ul>
 *          <li>stops Kafka listeners</li>
 *          <li>redirects to <code>/admin/kafka/listeners</code> when done</li>
 *      </ul>
 *  </li>
 * </ul>
 *           
 * 
 * @author jdamerow
 *
 */
@Controller
public class KafkaListenerController {
    
    @Autowired
    private IKafkaListenerManager listenerManager;

    @RequestMapping(value="/admin/kafka/listeners")
    public String showPage(Model model) {
        model.addAttribute("listenerStatus", listenerManager.isListening());
        return "admin/kafka/listeners";
    }
    
    @RequestMapping(value="/admin/kafka/listeners/start", method=RequestMethod.POST)
    public String startListeners() {
        listenerManager.startListeners();
        return "redirect:/admin/kafka/listeners";
    }
    
    @RequestMapping(value="/admin/kafka/listeners/stop", method=RequestMethod.POST)
    public String stopListeners() {
        listenerManager.shutdownListeners();
        return "redirect:/admin/kafka/listeners";
    }
}
