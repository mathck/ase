package at.tuwien.ase.controller;

import at.tuwien.ase.controller.exceptions.GenericRestExceptionHandler;

import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.model.Reward;
import at.tuwien.ase.services.RewardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * Created by Daniel Hofer on 20.11.2015.
 */

@RestController
@RequestMapping("/api/")
public class RewardController {

    @Autowired
    private RewardService rs;

    @Autowired
    private GenericRestExceptionHandler genericRestExceptionHandler;

    private static final Logger logger = LogManager.getLogger(RewardController.class);

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/rewards/{rID}", method = RequestMethod.GET)
    @ResponseBody
    public Reward getRewardByID(@PathVariable("rID") int rID) throws Exception {
        return rs.getByID(rID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/rewards", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public JsonStringWrapper createReward(@RequestBody Reward reward) throws Exception {
        return rs.writeReward(reward);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/users/{uID}/rewards/{rID}", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public void assignAwardToUser(@PathVariable("pID") int pID, @PathVariable("uID") String uID, @PathVariable("rID") int rID) throws Exception {
        rs.assignAwardToUser(pID, uID, rID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/rewards", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Reward> getAllRewards() {
        return rs.getAllRewards();
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/rewards", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Reward> getAllRewardsFromProject(@PathVariable("pID") int pID) {
        return rs.getAllRewardsFromProject(pID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/rewards", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Reward> getAllRewardsCreatedByUser(@RequestParam("uID") String uID) {
        return rs.getAllRewardsCreatedByUser(uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/users/{uID}/rewards", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Reward> getAllRewardsAwardedToUser(@PathVariable("uID") String uID) {
        return rs.getAllRewardsAwardedToUser(uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/projects/{pID}/users/{uID}/rewards", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<Reward> getRewardsByProjectAndUser(@PathVariable("pID") int pID, @PathVariable("uID") String uID) throws Exception {
        return rs.getAllRewardsFromProjectAndUser(pID, uID);
    }

    // @author Daniel Hofer
    @RequestMapping(value = "workspace/rewards/{rID}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteRewardByID(@PathVariable("rID") int rID)  throws Exception {
        rs.deleteRewardByID(rID);
    }

}
