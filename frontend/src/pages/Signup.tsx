import React from 'react';
import styled from 'styled-components';
import { useForm } from 'react-hook-form';
import { useMutation } from 'react-query';
import { useNavigate, Link } from 'react-router-dom';
import { authApi } from '../services/api';
import { SignupRequest } from '../types';
import toast from 'react-hot-toast';

const SignupContainer = styled.div`
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
`;

const SignupCard = styled.div`
  background: white;
  border-radius: 16px;
  padding: 40px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
`;

const Title = styled.h1`
  text-align: center;
  margin-bottom: 32px;
  color: #333;
  font-size: 28px;
  font-weight: 700;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 20px;
`;

const FormGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

const Label = styled.label`
  font-weight: 500;
  color: #333;
  font-size: 14px;
`;

const Input = styled.input`
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  transition: border-color 0.2s ease;

  &:focus {
    outline: none;
    border-color: #1976d2;
  }
`;

const Button = styled.button`
  padding: 12px 24px;
  background: #1976d2;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s ease;

  &:hover {
    background: #1565c0;
  }

  &:disabled {
    background: #ccc;
    cursor: not-allowed;
  }
`;

const LoginLink = styled.div`
  text-align: center;
  margin-top: 24px;
  color: #666;
  
  a {
    color: #1976d2;
    text-decoration: none;
    font-weight: 500;
    
    &:hover {
      text-decoration: underline;
    }
  }
`;

const Signup: React.FC = () => {
  const navigate = useNavigate();
  const { register, handleSubmit, formState: { errors } } = useForm<SignupRequest>();

  const signupMutation = useMutation(authApi.signup, {
    onSuccess: (data) => {
      localStorage.setItem('accessToken', data.accessToken);
      localStorage.setItem('refreshToken', data.refreshToken);
      toast.success('회원가입이 완료되었습니다!');
      navigate('/');
    },
    onError: (error: any) => {
      toast.error(error.response?.data?.message || '회원가입에 실패했습니다.');
    }
  });

  const onSubmit = (data: SignupRequest) => {
    signupMutation.mutate(data);
  };

  return (
    <SignupContainer>
      <SignupCard>
        <Title>회원가입</Title>
        
        <Form onSubmit={handleSubmit(onSubmit)}>
          <FormGroup>
            <Label>이메일</Label>
            <Input
              type="email"
              {...register('email', { 
                required: '이메일을 입력해주세요.',
                pattern: {
                  value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                  message: '올바른 이메일 형식이 아닙니다.'
                }
              })}
              placeholder="이메일을 입력하세요"
            />
            {errors.email && (
              <span style={{ color: '#d32f2f', fontSize: '12px' }}>
                {errors.email.message}
              </span>
            )}
          </FormGroup>

          <FormGroup>
            <Label>닉네임</Label>
            <Input
              {...register('nickname', { 
                required: '닉네임을 입력해주세요.',
                minLength: {
                  value: 2,
                  message: '닉네임은 최소 2자 이상이어야 합니다.'
                },
                maxLength: {
                  value: 20,
                  message: '닉네임은 최대 20자까지 가능합니다.'
                }
              })}
              placeholder="닉네임을 입력하세요"
            />
            {errors.nickname && (
              <span style={{ color: '#d32f2f', fontSize: '12px' }}>
                {errors.nickname.message}
              </span>
            )}
          </FormGroup>

          <FormGroup>
            <Label>비밀번호</Label>
            <Input
              type="password"
              {...register('password', { 
                required: '비밀번호를 입력해주세요.',
                minLength: {
                  value: 6,
                  message: '비밀번호는 최소 6자 이상이어야 합니다.'
                }
              })}
              placeholder="비밀번호를 입력하세요"
            />
            {errors.password && (
              <span style={{ color: '#d32f2f', fontSize: '12px' }}>
                {errors.password.message}
              </span>
            )}
          </FormGroup>

          <Button type="submit" disabled={signupMutation.isLoading}>
            {signupMutation.isLoading ? '가입 중...' : '회원가입'}
          </Button>
        </Form>

        <LoginLink>
          이미 계정이 있으신가요? <Link to="/login">로그인</Link>
        </LoginLink>
      </SignupCard>
    </SignupContainer>
  );
};

export default Signup;