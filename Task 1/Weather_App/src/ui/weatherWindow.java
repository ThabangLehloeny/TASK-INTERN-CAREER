package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class weatherWindow extends JFrame{
 
	private JTextField txtLocation;
	private JButton btnGetWeather;
	private JTextArea txtDisplayWeather;
	
	//ApiKey for API authorization
	private String ApiKey ="9e7ebf5a7b8bdc474f99beeda1fb97df";
	private JLabel  lblWeatherImage;
	
	//parameter less constructor
	public weatherWindow() {
		// TODO Auto-generated constructor stub
		
		//Setting up the window
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400,550);
		this.setLayout(new FlowLayout());
		
		txtLocation = new JTextField(15);
		 btnGetWeather = new JButton("Get weather");
		 txtDisplayWeather = new JTextArea(10,30);
		txtDisplayWeather.setEditable(false);
		lblWeatherImage = new JLabel();
		
		this.add(new JLabel("Enter a city name"));
		this.add(txtLocation);
		this.add(btnGetWeather);
		this.add(txtDisplayWeather);
		this.add(lblWeatherImage);
		
		///Handling the buttonEvent
		btnGetWeather.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String city = txtLocation.getText();
				String forecast = getWeatherForecast(city);
				
				//If statements to change images based on the  weather forecast will be here
				txtDisplayWeather.setText(forecast);
			}

		});
		
		this.setVisible(true);
	}
	

	private String getWeatherForecast(String city) {
		///This function is to fetch data from openweather map
		try {
			URL url = new URL("https://api.openweathermap.org/data/2.5/weather/?q=" + city + "&appid=" + ApiKey);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response = " ";
			String line;
			
			while((line = bf.readLine())!=null) {
				response += line;
			}
			bf.close();
			
			JSONObject jsonObject = (JSONObject) JSONValue.parse(response.toString());
			JSONObject mainObject  = (JSONObject) jsonObject.get("main");
			
			double temperature = (double) mainObject.get("temp") - 273.15; //Converting the temperature from kelvins to celcius.
			long humidity = (long) mainObject.get("humidity");
			
			///getting the weather description 
			JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
			JSONObject weather = (JSONObject) weatherArray.get(0);
			String description = (String) weather.get("description");
			String weatherCondition = description.toLowerCase();
			
			 ImageIcon icon = null;
			 //Display different icons for different weather types
	            switch (weatherCondition) {
	                case "broken clouds":
	                    icon = new ImageIcon("Images/broken_clouds.png");
	                    break;
	                case "few clouds":
	                    icon = new ImageIcon("Images/download.png");
	                    break;
	                case "clear sky":
	                	icon = new ImageIcon("Images/clear_sky.png");
	                	break;
	                case "scattered clouds":
	                	icon = new ImageIcon("Images/scattered_cloud.jpg");
	                	break;
	                	
	                case "light snow":
	                	icon = new ImageIcon("Images/light_snow.png");
	                	break;
	                case "very heavy rain":
	                	icon = new ImageIcon("Images/heavy_rain.png");
	                	break;
	                case "overcast clouds":
	                	icon = new ImageIcon("Images/overcast_clouds.jpg");
	                	break;
	                case "light rain":
	                	icon = new ImageIcon("Images/light_rain.png");
	                	break;
	                case "snow":
	                	icon = new ImageIcon("Images/snow.png");
	                	break;
	                case "hail":
	                	icon = new ImageIcon("Images/hail.png");
	                	break;
	                	
	                default:
	                	
	                	icon = new ImageIcon("Images/default.jpg");
	                    break;
	            }
	            
	            lblWeatherImage.setIcon(icon);
	            
	            //returning the weather forecast.
			return "Description: " + description + "\ntemperature: " + (Math.ceil(temperature / 10) * 10) + " Celcius\nhumidity: " + humidity + "%";
			
		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to fetch weather information";
		}

	}
}
