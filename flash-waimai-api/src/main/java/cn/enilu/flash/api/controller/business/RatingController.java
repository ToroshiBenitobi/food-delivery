package cn.enilu.flash.api.controller.business;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.entity.front.FrontUserInfo;
import cn.enilu.flash.bean.entity.front.Ratings;
import cn.enilu.flash.bean.entity.front.sub.Rating;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.dao.MongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created  on 2018/1/5 0005.
 *
 * @Author enilu
 */
@RestController
public class RatingController extends BaseController {
    @Autowired
    private MongoRepository mongoRepository;

    @RequestMapping(value = "/ugc/v2/restaurants/{restaurant_id}/ratings", method = RequestMethod.GET)
    public Object ratings(@PathVariable("restaurant_id") Long restaurantId) {
        Map map = mongoRepository.findOne("ratings", "restaurant_id", restaurantId);
        return Rets.success(map.get("ratings"));
    }

    @RequestMapping(value = "/ugc/v2/restaurants/{restaurant_id}/ratings/scores", method = RequestMethod.GET)
    public Object score(@PathVariable("restaurant_id") Long restaurantId) {
        Map map = mongoRepository.findOne("ratings", "restaurant_id", restaurantId);
        return map.get("scores");
    }

    @RequestMapping(value = "/ugc/v2/restaurants/{restaurant_id}/ratings/tags", method = RequestMethod.GET)
    public Object tags(@PathVariable("restaurant_id") Long restaurantId) {
        Ratings ratings = mongoRepository.findOne(Ratings.class, "restaurant_id", restaurantId);
//        return map.get("tags");
        return Rets.success(ratings.getTags());
    }

    @RequestMapping(value = "/ugc/v2/restaurants/{restaurant_id}/ratings/ratings", method = RequestMethod.POST)
    public Object addRatings(@RequestParam("username") String username,
                           @PathVariable("restaurant_id") Integer restaurantId,
                           @RequestParam("rating_star") Integer ratingStar,
                           @RequestParam("rating_text") String ratingText) {
        Map ratings = mongoRepository.findOne("ratings", "restaurant_id", restaurantId);
        Map<String, Object> rating = new LinkedHashMap();
        rating.put("rating_star", ratingStar);
        rating.put("rating_text", ratingText);
        rating.put("time_spent_desc",ratingText);
        rating.put("username", username);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        rating.put("rated_at", date);
        FrontUserInfo frontUserInfo = mongoRepository.findOne(FrontUserInfo.class, "username", username);
        rating.put("avatar", frontUserInfo.getAvatar());
        ((ArrayList) ratings.get("ratings")).add(rating);
        mongoRepository.update(Integer.toUnsignedLong((Integer) ratings.get("restaurant_id")), "ratings", ratings);
        mongoRepository.save(ratings, "ratings");
        return Rets.success();
    }
}