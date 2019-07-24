#include "MatrixState.hpp"

int MatrixState::stackTop=-1;
float MatrixState::currMatrix[16];
float MatrixState::mProjMatrix[16];
float MatrixState::mVMatrix[16];
float MatrixState::mMVPMatrix[16];
float MatrixState::mStack[10][16];