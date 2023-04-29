package ai;

public class AI{
	private float[][] v;
	private float[][] z;
	private float[][] b;
	private float[][] bc;
	private float[][][] w;
	private float[][][] wc;
	
	private int ans;
	private int[] inds;
	
	public AI(int[] inds){
		this.inds = inds.clone();
		v = new float[inds.length][];
		for(int i = 0; i < inds.length; i++)
			v[i] = new float[inds[i]];
		
		
		z = new float[inds.length-1][];
		b = new float[inds.length-1][];
		bc = new float[inds.length-1][];
		w = new float[inds.length-1][][];
		wc = new float[inds.length-1][][];
		for(int i = 0; i < inds.length-1; i++){
			z[i] = new float[inds[i+1]];
			b[i] = new float[inds[i+1]];
			bc[i] = new float[inds[i+1]];
			w[i] = new float[inds[i+1]][inds[i]];
			wc[i] = new float[inds[i+1]][inds[i]];
			for(int j = 0; j < inds[i+1]; j++){
				b[i][j] = (float)(Math.random()-0.5)*2.0f;
				bc[i][j] = 0.0f;
				for(int k = 0; k < inds[i]; k++){
					w[i][j][k] = (float)(Math.random()-0.5)*2.0f;
					wc[i][j][k] = 0.0f;
				}
			}
		}
	}
	
	public void setInput(float[] input){
		for(int i = 0; i < v[0].length; i++)
			v[0][i] = input[i];
	}
	
	public int getAns(){
		for(int i = 0; i < inds.length-1; i++){
			for(int j = 0; j < inds[i+1]; j++){
				z[i][j] = 0.0f;
				for(int k = 0; k < inds[i]; k++)
					z[i][j] += w[i][j][k]*v[i][k];
				z[i][j] += b[i][j];
				v[i+1][j] = sigmoid(z[i][j]);
			}
		}
		double[] p = new double[inds[inds.length-1]-1];
		double S = 0.0;
		for(int i = 0; i < inds[inds.length-1]-1; i++)
			p[i] = v[inds.length-1][i];
		for(int i = 1; i < inds[inds.length-1]-1; i++)
			p[i] += p[i-1];
		for(int i = 0; i < inds[inds.length-1]; i++)
			S += v[inds.length-1][i];
		for(int i = 0; i < inds[inds.length-1]-1; i++)
			p[i] /= S;
		double res = Math.random();
		ans = 0;
		for(int i = 0; i < inds[inds.length-1]-1; i++)
			if(res > p[i])ans++;
		return ans;
	}
	public void backPropagate(boolean signal){
		float[][] vc = new float[inds.length][];
		for(int i = 0; i < inds.length; i++)
			vc[i] = new float[inds[i]];
		if(signal){
			for(int i = 0; i < inds[inds.length-1]; i++)
				vc[inds.length-1][i] = 2.0f*v[inds.length-1][i];
			vc[inds.length-1][ans] = 2.0f*(v[inds.length-1][ans] - 1.0f);
		} else{
			for(int i = 0; i < inds[inds.length-1]; i++)
				vc[inds.length-1][i] = 2.0f*(v[inds.length-1][i] - 1.0f);
			vc[inds.length-1][ans] = 2.0f*(v[inds.length-1][ans] - 0.0f);
		}
		for(int i = inds.length-1; i > 0; i--){
			for(int j = 0; j < inds[i]; j++){
				vc[i][j] *= dSigmoid(z[i-1][j]);
				bc[i-1][j] += vc[i][j];
				for(int k = 0; k < inds[i-1]; k++){
					wc[i-1][j][k] += vc[i][j]*v[i-1][k];
					vc[i-1][j] = vc[i][j]*w[i-1][j][k];
				}
			}
		}
		for(int i = 0; i < inds.length-1; i++){
			for(int j = 0; j < inds[i+1]; j++){
				b[i][j] -= bc[i][j];
				bc[i][j] = 0.0f;
				for(int k = 0; k < inds[i]; k++){
					w[i][j][k] -= wc[i][j][k];
					wc[i][j][k] = 0.0f;
				}
			}
		}
	}
	public void printAll(){
		System.out.println("Biases:");
		for(int i = 0; i < inds.length-1; i++){
			for(int j = 0; j < inds[i+1]; j++)
				System.out.print(b[i][j]+ " ");
			System.out.println("");
		}
		System.out.println("Weights:");
		for(int i = 0; i < inds.length-1; i++){
			for(int j = 0; j < inds[i+1]; j++){
				for(int k = 0; k < inds[i]; k++)
					System.out.print(w[i][j][k]+ " ");
				System.out.println("");
			}
			System.out.println("");
		}
	}
	private float dSigmoid(float x){
		if(x*x > 16)
			return 0.0f;
		float d = sigmoid(x);
		d *= d;
		d *= exp(-x);
		return d;
	}
	private float sigmoid(float x){
		if(x*x > 16){
			if(x > 0)
				return 1.0f;
			else
				return 0.0f;
		}
		return 1.0f/(1.0f+exp(-x));
	}
	private float exp(float x){
		return expSeries(x, 1.0f);
	}
	private float expSeries(float x, float n){
		if((int)n == 20)
			return 0;
		else
			return 1.0f + (x / n) * expSeries(x, ++n);
	}
}