package me.edoardo.pieces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/hashes")
	public ArrayList<Map<String, Object>> index() {
		MerkleSingleton mkt = MerkleSingleton.getInstance();
		ArrayList<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map = new HashMap<>();
		map.put("pieces", Integer.valueOf(mkt.numPieces()));
		map.put("hash", mkt.rootAsString());
		res.add(map);
		return res;
	}

}