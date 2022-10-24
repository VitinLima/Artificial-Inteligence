float DSigmoid(float P){
	if(P*P > 16){
		return 0.0f;
	}
	float Z = 1/(1+expi(-P));
	Z *= Z;
	Z *= expi(-P);
	return Z;
}

void Corrige(){
	//printf("Corrige\n");
	int i, j, k;
	for(i = 0; i < Nc-1; i++)
		for(j = 0; j < I[i+1]; j++){
			for(k = 0; k < I[i]; k++)
				W[i][j][k] -= WC[i][j][k];
			B[i][j] -= BC[i][j];
		}
}

void BackPropagation(boolean Incentivo, float Magnitude, int Choice, int BatchSize){
	//printf("BackPropagation\n");
	int i, j, k;
	float z;
	
	for(i = 0; i < Nc-1; i++)
		for(j = 0; j < I[i+1]; j++)
			LC[i][j] = 0;
	
	if(Incentivo){
		for(i = 0; i < 4; i++)
			Answer[i] = 0;
		Answer[Choice-1] = 1;
	}
	else{
		for(i = 0; i < 4; i++)
			Answer[i] = 1;
		Answer[Choice-1] = 0;
	}
	
	for(i = 0; i < I[Nc-1]; i++)
		LC[Nc-2][i] = 2*(L[Nc-1][i]-Answer[i])*Magnitude;
	
	for(i = Nc-2; i >= 0; i--){
		for(j = 0; j < I[i+1]; j++){
			z = 0.0f;
			for(k = 0; k < I[i]; k++)
				z += L[i][k]*W[i][j][k];
			z += B[i][j];
			BC[i][j] += LC[i][j]*DSigmoid(z)/(float)BatchSize;
			for(k = 0; k < I[i]; k++)
				WC[i][j][k] += LC[i][j]*L[i][k]*DSigmoid(z)/(float)BatchSize;
		}
		if(i > 0)
			for(j = 0; j < I[i+1]; j++)
				for(k = 0; k < I[i]; k++)
					LC[i-1][k] += LC[i][j]*W[i][j][k]*DSigmoid(z);
	}
}

void Processa(){
	//printf("Processa\n");
	int i, j, k;
	
	for(i = 0; i < Nc-1; i++)
		for(j = 0; j < I[i+1]; j++){
			L[i+1][j] = 0;
			for(k = 0; k < I[i]; k++)
				L[i+1][j] += L[i][k]*W[i][j][k];
			L[i+1][j] += B[i][j];
			L[i+1][j] = Sigmoid(L[i+1][j]);
		}
}

void IniciaWB(boolean opt, FILE *A){
	//printf("IniciaWB\n");
	int i, j, k;
	if(opt){
		for(i = 0; i < Nc-1; i++)
			for(j = 0; j < I[i+1]; j++)
				for(k = 0; k < I[i]; k++)
					fscanf(A, "%f", &W[i][j][k]);
		for(i = 0; i < Nc-1; i++)
			for(j = 0; j < I[i+1]; j++)
				fscanf(A, "%f", &B[i][j]);
	}
	else{
		for(i = 0; i < Nc-1; i++)
			for(j = 0; j < I[i+1]; j++){
				B[i][j] = ((float)rand()/(float)RAND_MAX-0.5f)*2;
				for(k = 0; k < I[i]; k++)
					W[i][j][k] = ((float)rand()/(float)RAND_MAX-0.5f)*2;
			}
	}
}

void ZeraWCBC(){
	//printf("ZeraWCBC\n");
	int i, j, k;
	for(i = 0; i < Nc-1; i++)
		for(j = 0; j < I[i+1]; j++){
			BC[i][j] = 0;
			for(k = 0; k < I[i]; k++)
				WC[i][j][k] = 0;
		}
}

void Mutation(){
	//printf("Mutation\n");
	/*int R[4];
	R[0] = (int)((float)rand()/(float)RAND_MAX*(float)(N-1));
	R[1] = (int)((float)rand()/(float)RAND_MAX*(float)(I[R[0]+1]-1));
	R[2] = (int)((float)rand()/(float)RAND_MAX);
	if(R[2] == 0){
		R[3] = (int)((float)rand()/(float)RAND_MAX*(float)(I[R[0]]-1));
		W[R[0]][R[1]][R[3]] *= ((float)rand()/(float)RAND_MAX*2.0f);
		W[R[0]][R[1]][R[3]] += ((float)rand()/(float)RAND_MAX*2.0f);
	}
	else{
		B[R[0]][R[1]] *= ((float)rand()/(float)RAND_MAX*2.0f);
		B[R[0]][R[1]] += ((float)rand()/(float)RAND_MAX*2.0f);
	}*/
}