package enums;

 enum solarsystem {
 Mercury,venus,earth, mars, jupiter
 ,saturn 
 ,urenus, neptune, pluto
}
public class planets{
	public solarsystem enumname;
	public double diameter;
	public double weight;
	public solarsystem getPlanets() {
		return enumname;
	}
	planets( solarsystem mercury, double dia, double weight){
		this.weight=weight;
		this.diameter=dia;
		this.enumname=mercury;
		
	}
	public void setPlanets(solarsystem planets) {
		this.enumname = planets;
	}
	public double getDiameter() {
		return diameter;
	}
	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public static void main(String[] args) {
		planets pl= new planets(solarsystem.Mercury,10000,20000);
		planets p2= new planets(solarsystem.venus,10000,20000);
		System.out.println(p2.getPlanets()+" has "+p2.getDiameter()+" km  diameter and "+p2.getWeight()+" kg weight");
	}}	
