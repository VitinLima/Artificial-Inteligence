#include "defines.h"

float Det(float **Matrix, int N){
	int i, j, k, b;
	float Determinante = 1, Ni, Nj;
	for(i = 0; i < N; i++){
		Ni = Matrix[i][i];
		printf("Ni: %.2f\n", Ni);
		if(Ni == 0){
			b = -1;
			for(j = 0; j < N; j++)
				if(Matrix[i][j] != 0){
					b = j;
					break;
			}
			if(b == -1)
				return 0;
			for(j = i; j < N; j++){
				Matrix[j][i] += Matrix[j][b];
				Matrix[j][b] = Matrix[j][i] - Matrix[j][b];
				Matrix[j][i] -= Matrix[j][b];
			}
			Ni = Matrix[i][i];
		}
		Determinante *= Ni;
		for(j = i; j < N; j++)
			Matrix[i][j] /= Ni;
		for(j = i+1; j < N; j++){
			Nj = Matrix[j][i];
			for(k = i; k < N; k++)
				Matrix[j][k] -= Matrix[i][k]*Nj;
		}
		printf("Matrix:\n");
		for(j = 0; j < N; j++){
			for(k = 0; k < N; k++)
				printf("%.2lf ", Matrix[j][k]);
			printf("\n");
		}
	}
	return Determinante;
}

float ProdutoEscalar(){
	return 0;
}

void ProdutoInterno(){

}

void ProdutoMatrixVetor(float **M, float *N, float *O, int Nm, int Nn){
	int i, j;
	float S;
	for(i = 0; i < Nm; i++){
		S = 0;
		for(j = 0; j < Nn; j++)
			S += M[i][j]*N[j];
		O[i] = S;
	}
}

void ProdutoVetorMatrix(float *N, float **M, float *O, int Nn, int Nm){
	int i, j;
	float S;
	for(i = 0; i < Nn; i++){
		S = 0;
		for(j = 0; j < Nm; j++)
			S += N[i]*M[i][j];
		O[i] = S;
	}
}

void ProdutoMatrixMatrix(){

}

void SomaVetor(){

}

void SomaMatrix(){

}

int faci(int a){
	if(a == 0)
		return 1;
	return a*faci(a-1);
}

float powi(float x, int a){
	if(a == 0)
		return 1;
	return x*powi(x, a-1);
}

float expj(float a, int i){
	if(i == 10)
		return 1;
	float s = powi(a,i);
	return (s + expj(a,i+1))/(float)i;
}

float expi(float a){
	return 1.0f + expj(a, 1);
}

float Sigmoid(float P){
	if(P*P > 16){
		if(P < 0)
			return 0.0f;
		else
			return 1.0f;
	}
	return 1/(1+expi(-P));
}