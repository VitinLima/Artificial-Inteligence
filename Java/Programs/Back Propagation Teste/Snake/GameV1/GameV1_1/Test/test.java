public class test{
	public static void main(String[] args){
		NeuralNetwork neuralnetwork = new NeuralNetwork(0);
		neuralnetwork.Basic_Constructor_1(new int[]{3, 2});
		neuralnetwork.setRandomValues();
		
		float[] activation = new float[neuralnetwork.getReceptorNeuronsIds().length];
		for(int i = 0; i < 10000; i++){
			for(int j = 0; j < activation.length; j++)
				activation[j] = (float)Math.random();
			neuralnetwork.setReceptorNeuronsValues(activation);
			neuralnetwork.Modify(new float[]{0.0f,1.0f});
		}
		
		System.out.println("\n\n" + neuralnetwork.printNetworkInformation() + "\n\n");
		for(int j = 0; j < activation.length; j++)
			activation[j] = (float)Math.random();
		for(float f:activation)
			System.out.print(f + " ");
		System.out.println("");
		neuralnetwork.setReceptorNeuronsValues(activation);
		for(float f:neuralnetwork.getAnswer())
			System.out.print(f + " ");
		System.out.println("");
	}
}