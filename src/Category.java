package mobi.kinetica.vegenat.catalogo.models;

import java.util.ArrayList;
import java.util.List;

import mobi.kinetica.vegenat.catalogo.R;
import mobi.kinetica.vegenat.catalogo.models.interfaces.Model;
import mobi.kinetica.vegenat.catalogo.models.interfaces.Synchronizable;
import mobi.kinetica.vegenat.catalogo.utils.Configuration;
import mobi.kinetica.vegenat.catalogo.utils.TextUtils;
import mobi.kinetica.vegenat.catalogo.utils.Update;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class Product extends SynchronizableSQLiteModel {

	public static String TAG = Configuration.LOG_TAG;
	
	public static String TABLE_NAME = "products";
	
	public static String COL_ID = "_id";
	public static String COL_NAME = "name";
	public static String COL_DESCRIPTION = "description";
	public static String COL_COLOR = "color";
	public static String COL_SNS = "sns";
	public static String COL_ACTIVE = "active";
	public static String COL_EDITED = "edited";
	public static String COL_VERSION = "version";
	public static String COL_CATEGORY_ID = "category_id";
	public static String COL_MODIFIED = "modified";
	public static String COL_CREATED = "created";
	
	public static String getCreateSql(){
		return "CREATE TABLE " + TABLE_NAME
				+ " ("
				+ COL_ID 			+ " integer primary key autoincrement, "
				+ COL_NAME 			+ " text, "
				+ COL_DESCRIPTION 	+ " text, "
				+ COL_COLOR 		+ " text, "
				+ COL_SNS		 	+ " integer, "
				+ COL_ACTIVE 		+ " text, "
				+ COL_EDITED		+ " text, "
				+ COL_VERSION		+ " integer, "
				+ COL_CATEGORY_ID	+ " integer, "
				+ COL_MODIFIED		+ " text, "
				+ COL_CREATED		+ " text "
				+");";
	}
	
	public String name;
	public String description;
	public String color;
	public String sns;
	public String active;
	public String edited;
	public int version;
	public int category_id;
	public String modified;
	public String created;
	
	public int category_checking;
	
	public Product(Context context){
		super(context);
	}
	
	public boolean parseFromJson(JSONObject json){
		try{
			JSONObject categoryJSON = json.getJSONObject("Product");
			this._id = categoryJSON.getInt("id");
			this.name = categoryJSON.getString("name");
			this.description = categoryJSON.getString("description");
			this.color = categoryJSON.getString("color");
			this.sns = categoryJSON.getString("sns");
			this.active = categoryJSON.getString("active");
			this.edited = categoryJSON.getString("edited");
			this.version = categoryJSON.getInt("version");
			this.category_id = categoryJSON.getInt("category_id");
			this.modified = categoryJSON.getString("modified");
			this.created = categoryJSON.getString("created");
		}catch(Exception e){
			
		}
		return true;
	}
	
	@Override
	public boolean getSync() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existsNewVersion(Synchronizable source) {
		Product categoryToCheck  = (Product)source;
		long id = categoryToCheck.getId();
		Product currentCategory = (Product)this.read(id);
		if(currentCategory._id == -1 || currentCategory.version < categoryToCheck.version){
			return true;
		}else{
			//Configuration.log(TAG, categoryToCheck.name + " - Dispon’a òltima Versi—n");
		}
		return false;
	}

	@Override
	public void setSinchronizingStatus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSinchronizedStatus() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean syncSubEntities() {
		boolean entities = true;
		List<Synchronizable> elements = Element.getElementsFromServer(context, (int)this._id);
		Element element = new Element(context);
		element.product_checking = (int)this._id;
		entities = element.syncAll(elements, element);
		elements = null;
		System.gc();
		return entities;
	}

	@Override
	public void parseCursor(Cursor cursor) {
		fill(
			cursor.getLong(cursor.getColumnIndex(Product.COL_ID)), 
			cursor.getString(cursor.getColumnIndex(Product.COL_NAME)), 
			cursor.getString(cursor.getColumnIndex(Product.COL_DESCRIPTION)),
			cursor.getString(cursor.getColumnIndex(Product.COL_COLOR)),
			cursor.getString(cursor.getColumnIndex(Product.COL_SNS)),
			cursor.getString(cursor.getColumnIndex(Product.COL_ACTIVE)),
			cursor.getString(cursor.getColumnIndex(Product.COL_EDITED)),
			cursor.getInt(cursor.getColumnIndex(Product.COL_VERSION)),
			cursor.getInt(cursor.getColumnIndex(Product.COL_CATEGORY_ID)),
			cursor.getString(cursor.getColumnIndex(Product.COL_MODIFIED)),
			cursor.getString(cursor.getColumnIndex(Product.COL_CREATED))
		);
	}

	private void fill(long _id, String name, String description, String color, String sns,
			String active, String edited, int version, int category_id, String modified,
			String created) {
		this._id = _id;
		this.name = name;
		this.description = description;
		this.color = color;
		this.sns = sns;
		this.active = active;
		this.edited = edited;
		this.version = version;
		this.category_id = category_id;
		this.modified = modified;
		this.created = created;
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public ContentValues parse2ContentValues() {
		ContentValues cv = new ContentValues();
		if(this._id != 0)
			cv.put(COL_ID, this._id);
		cv.put(COL_NAME, this.name);
		cv.put(COL_DESCRIPTION, this.description);
		cv.put(COL_COLOR, this.color);
		cv.put(COL_SNS, this.sns);
		cv.put(COL_ACTIVE, this.active);
		cv.put(COL_EDITED, this.edited);
		cv.put(COL_VERSION, this.version);
		cv.put(COL_CATEGORY_ID, this.category_id);
		cv.put(COL_MODIFIED, this.modified);
		cv.put(COL_CREATED, this.created);
		return cv;
	}

	@Override
	public View populateItem(View view) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public View populateItem(Activity activity) {
		TextView title = (TextView)activity.findViewById(R.id.category_title);
		title.setText(this.name);
		return null;
	}

	@Override
	public View populateListItem(View view) {
		TextView textView = (TextView)view;
		textView.setId((int)this._id);
		textView.setText(this.name);
		//textView.setBackgroundColor(Color.argb(200, 255, 255, 255));
		textView.setBackgroundResource(R.drawable.product_item);
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(25);
		textView.setPadding(10, 15, 0, 15);
		textView.setGravity(Gravity.LEFT);
		TextUtils.setCustomFontLightToTextView(context, textView);
		return textView;
	}

	@Override
	public List<Model> cursor2ListOptions(Cursor cursor) {
		ArrayList<Model> list = new ArrayList<Model>();
		if (cursor.getCount() > 0){
			cursor.moveToFirst();
			while (!cursor.isAfterLast()){
				Model category = new Product(this.context);
				category.parseCursor(cursor);
				list.add(category);
				cursor.moveToNext();
			}
		}
		return list;
	}

	@Override
	public int markAllRemovable() {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(COL_ACTIVE, "false");
		return this.updateQuery(TABLE_NAME, cv, "products.category_id="+this.category_checking, null);
	}

	@Override
	public void markAsNoRemovable(Synchronizable model) {
		// TODO Auto-generated method stub
		//Do nothing. JSON only retrieve rows with field active = true
		//Configuration.log("Un Producto");
		Product category = (Product)model;
		ContentValues cv = new ContentValues();
		cv.put(COL_ACTIVE, "true");
		category.updateQuery(TABLE_NAME, cv, "_id = "+ category.getId(), null);
	}

	@Override
	public int removeModelsMarkedAsRemovable() {
		// TODO Auto-generated method stub
		String where = COL_ACTIVE + " = ? ";
		String[] args = {"false"};
		return this.deleteQuery(TABLE_NAME, where, args);
	}
	
	public static List<Synchronizable> getProductsFromServer(Context context, int category){
		List<Synchronizable> products = new ArrayList<Synchronizable>();
		String queryUrl = "http://" + Configuration.getServer() + Configuration.UPDATE_PRODUCTS_ACTION.replace("?", category + "");
		String response = Update.getContent(context, queryUrl);
    	//Configuration.log(response);
    	Product product = new Product(context);
    	JSONObject json;
    	try{
    		JSONArray jsonArray = new JSONArray(response);
    		int numberOfProducts = jsonArray.length();
    		for (int index = 0; index < numberOfProducts; index++){  
    			product = new Product(context);
    			json = jsonArray.getJSONObject(index);
    			product.parseFromJson(json);
    			//Configuration.log("Version in Product " + product._id + " after parser " + product.version);
    			products.add(product);
    		}
    	}catch(Exception e){
    		Configuration.log(e.toString());
    	}
    	return products;
	}
	
	@Override
	public boolean save(Model model){
		boolean save = super.save(model);
		//Product product = (Product)model;
		return save;
	}
	
	public List<Model> getExtras(){
		String sql = 
				  " SELECT * "
				+ " FROM "	   + Element.TABLE_NAME 
				+ " WHERE "    + Element.TABLE_NAME + "." + Element.COL_PRODUCT_ID  + " = " + this._id
				+ " AND "      + Element.TABLE_NAME + "." + Element.COL_EXTRA + " = 'true' "  
				+ " ORDER BY " + Element.TABLE_NAME + "." + Element.COL_COL;
		//Configuration.log(sql);	
		Element staticElement = new Element(context);
		return staticElement.find(sql);
	}

	@Override
	public void setMessageOnStart() {
		// TODO Auto-generated method stub
		Configuration.log(TAG, this.name);
	}
	
}