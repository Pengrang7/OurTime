import React, { useState } from 'react';
import styled from 'styled-components';
import { useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from 'react-query';
import { groupApi } from '../services/api';
import toast from 'react-hot-toast';

interface GroupCreateModalProps {
  isOpen: boolean;
  onClose: () => void;
}

interface GroupFormData {
  name: string;
  type: string;
  description?: string;
  inviteeEmails: string;
}

const GroupCreateModal: React.FC<GroupCreateModalProps> = ({ isOpen, onClose }) => {
  const queryClient = useQueryClient();
  const { register, handleSubmit, reset, formState: { errors } } = useForm<GroupFormData>();

  const createGroupMutation = useMutation(
    async (data: GroupFormData) => {
      const inviteeEmailsArray = data.inviteeEmails
        ? data.inviteeEmails.split(',').map(email => email.trim()).filter(email => email)
        : [];

      return groupApi.createGroupWithInvites({
        name: data.name,
        type: data.type,
        description: data.description,
        inviteeEmails: inviteeEmailsArray
      });
    },
    {
      onSuccess: () => {
        toast.success('그룹이 생성되었습니다!');
        queryClient.invalidateQueries('groups');
        reset();
        onClose();
      },
      onError: (error: any) => {
        toast.error(error.response?.data?.message || '그룹 생성에 실패했습니다.');
      }
    }
  );

  const onSubmit = (data: GroupFormData) => {
    createGroupMutation.mutate(data);
  };

  if (!isOpen) return null;

  return (
    <ModalOverlay onClick={onClose}>
      <ModalContent onClick={(e) => e.stopPropagation()}>
        <ModalHeader>
          <h2>새 그룹 만들기</h2>
          <CloseButton onClick={onClose}>&times;</CloseButton>
        </ModalHeader>

        <Form onSubmit={handleSubmit(onSubmit)}>
          <FormGroup>
            <Label>그룹 이름 *</Label>
            <Input
              type="text"
              placeholder="그룹 이름을 입력하세요"
              {...register('name', { required: '그룹 이름은 필수입니다' })}
            />
            {errors.name && <ErrorText>{errors.name.message}</ErrorText>}
          </FormGroup>

          <FormGroup>
            <Label>그룹 타입 *</Label>
            <Select {...register('type', { required: '그룹 타입은 필수입니다' })}>
              <option value="">선택하세요</option>
              <option value="COUPLE">커플</option>
              <option value="FAMILY">가족</option>
              <option value="FRIEND">친구</option>
              <option value="TEAM">팀</option>
              <option value="ETC">기타</option>
            </Select>
            {errors.type && <ErrorText>{errors.type.message}</ErrorText>}
          </FormGroup>

          <FormGroup>
            <Label>그룹 설명</Label>
            <Textarea
              placeholder="그룹에 대한 설명을 입력하세요"
              {...register('description')}
              rows={3}
            />
          </FormGroup>

          <FormGroup>
            <Label>멤버 초대 (선택)</Label>
            <Input
              type="text"
              placeholder="이메일을 쉼표(,)로 구분하여 입력 (예: user1@example.com, user2@example.com)"
              {...register('inviteeEmails')}
            />
            <HelpText>초대할 멤버의 이메일 주소를 입력하세요. 여러 명은 쉼표로 구분합니다.</HelpText>
          </FormGroup>

          <ButtonGroup>
            <CancelButton type="button" onClick={onClose}>
              취소
            </CancelButton>
            <SubmitButton type="submit" disabled={createGroupMutation.isLoading}>
              {createGroupMutation.isLoading ? '생성 중...' : '그룹 만들기'}
            </SubmitButton>
          </ButtonGroup>
        </Form>
      </ModalContent>
    </ModalOverlay>
  );
};

// Styled Components
const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
`;

const ModalContent = styled.div`
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
`;

const ModalHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 1px solid #e5e7eb;

  h2 {
    margin: 0;
    font-size: 24px;
    font-weight: 600;
    color: #111827;
  }
`;

const CloseButton = styled.button`
  background: none;
  border: none;
  font-size: 32px;
  color: #6b7280;
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  
  &:hover {
    color: #111827;
  }
`;

const Form = styled.form`
  padding: 24px;
`;

const FormGroup = styled.div`
  margin-bottom: 20px;
`;

const Label = styled.label`
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #374151;
`;

const Input = styled.input`
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
  
  &:focus {
    outline: none;
    border-color: #3b82f6;
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  }
`;

const Select = styled.select`
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
  background: white;
  
  &:focus {
    outline: none;
    border-color: #3b82f6;
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  }
`;

const Textarea = styled.textarea`
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
  resize: vertical;
  
  &:focus {
    outline: none;
    border-color: #3b82f6;
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  }
`;

const HelpText = styled.p`
  margin-top: 6px;
  font-size: 12px;
  color: #6b7280;
`;

const ErrorText = styled.p`
  margin-top: 6px;
  font-size: 12px;
  color: #ef4444;
`;

const ButtonGroup = styled.div`
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 24px;
`;

const Button = styled.button`
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  
  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
`;

const CancelButton = styled(Button)`
  background: white;
  border: 1px solid #d1d5db;
  color: #374151;
  
  &:hover:not(:disabled) {
    background: #f9fafb;
  }
`;

const SubmitButton = styled(Button)`
  background: #3b82f6;
  border: none;
  color: white;
  
  &:hover:not(:disabled) {
    background: #2563eb;
  }
`;

export default GroupCreateModal;

