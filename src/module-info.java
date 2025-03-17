/**
 * 
 */
/**
 * 
 */
module MusicLibraryApp {
	requires java.base;
	requires org.junit.jupiter.api;
	requires jdk.incubator.vector;
	requires junit;
	requires org.json;

	
	opens main.model to org.junit.jupiter, org.json;
}