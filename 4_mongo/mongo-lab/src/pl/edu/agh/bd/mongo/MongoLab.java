package pl.edu.agh.bd.mongo;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import com.mongodb.*;

public class MongoLab {
	private MongoClient mongoClient;
	private DB db;
	
	public MongoLab() throws UnknownHostException {
		mongoClient = new MongoClient();
		db = mongoClient.getDB("MongoLab");
	}
	
	private void showCollections(){
		for(String name : db.getCollectionNames()){
			System.out.println("colname: "+name);
		}
	}

	public static void main(String[] args) throws UnknownHostException {
		MongoLab mongoLab = new MongoLab();
//		mongoLab.showCollections();

		mongoLab.zad1a();
		mongoLab.zad1b();
		mongoLab.zad1c();
		mongoLab.zad1d();
		mongoLab.zad1e();
		mongoLab.zad1f();
		mongoLab.zad1g();

	}

	public void zad1a(){
		DBCollection business = db.getCollection("business");
		List result = business.distinct("city");
		System.out.println(result.size());
		result.forEach(System.out::println);
	}

	public void zad1b(){
		BasicDBObject clause1 = new BasicDBObject("date", new BasicDBObject("$regex", "2011.*"));
		BasicDBObject clause2 = new BasicDBObject("date", new BasicDBObject("$regex", "2012.*"));
		BasicDBObject query = new BasicDBObject("$or", Arrays.asList(clause1, clause2));
		System.out.println(db.getCollection("review").count(query));
	}

	public void zad1c(){
		DBObject query = new BasicDBObject("open", true);
		DBCursor cursor = db.getCollection("business").find(query);
		cursor.forEach(record -> System.out.println(
				String.format("id: %s | name: %s | address: %s",
						record.get("business_id"), record.get("name"), record.get("full_address"))
		));
	}

	public void zad1d(){
		BasicDBObject funny = new BasicDBObject("votes.funny", new BasicDBObject("$gt", 0));
		BasicDBObject useful = new BasicDBObject("votes.useful", new BasicDBObject("$gt", 0));
		BasicDBObject cool = new BasicDBObject("votes.cool", new BasicDBObject("$gt", 0));

		DBObject match = new BasicDBObject("$match",
				new BasicDBObject("$or", Arrays.asList(funny, useful, cool)));
		DBObject sort = new BasicDBObject("$sort", new BasicDBObject("name", 1));

		List<DBObject> pipeline = Arrays.asList(match, sort);
		Cursor output = db.getCollection("user")
				.aggregate(pipeline, AggregationOptions.builder().allowDiskUse(true).build());

		while(output.hasNext()) System.out.println(output.next());
	}

	public void zad1e(){
		DBObject lookup = new BasicDBObject("$lookup",
				new BasicDBObject("from", "tip")
						.append("localField", "business_id")
						.append("foreignField", "business_id")
						.append("as", "tips"));

		DBObject match = new BasicDBObject("$match", new BasicDBObject("tips.date", new BasicDBObject("$regex", "2013.*")));

		DBObject project = new BasicDBObject("$project",
				new BasicDBObject("_id",0)
						.append("name",1)
						.append("tipsAmount",new BasicDBObject("$size", "$tips")));

		DBObject sort = new BasicDBObject("$sort", new BasicDBObject("name", 1));

		List<DBObject> pipeline = Arrays.asList(lookup, match, project, sort);
		Cursor output = db.getCollection("business").aggregate(pipeline, AggregationOptions.builder().build());

		while(output.hasNext()) System.out.println(output.next());
	}

	public void zad1f(){
		DBObject groupFields = new BasicDBObject("_id", "$business_id");
		groupFields.put("avg", new BasicDBObject("$avg", "$stars"));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject sort = new BasicDBObject("$sort", new BasicDBObject("avg", -1));

		List<DBObject> pipeline = Arrays.asList(group, sort);
		Cursor output = db.getCollection("review").aggregate(pipeline, AggregationOptions.builder().build());

		while(output.hasNext()) System.out.println(output.next());
	}

	public void zad1g(){
		db.getCollection("business").remove(new BasicDBObject("stars", new BasicDBObject("lt", 3)));
	}
}
