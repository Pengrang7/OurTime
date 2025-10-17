import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from 'react-query';
import { Group, Memory, CreateMemoryRequest } from '../types';
import { memoryApi } from '../services/api';
import toast from 'react-hot-toast';

interface MemoryModalProps {
  isOpen: boolean;
  onClose: () => void;
  location?: { lat: number; lng: number } | null;
  memory?: Memory | null;
  groups: Group[];
  onMemoryCreated: () => void;
  onMemoryUpdated: () => void;
}

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10000;
`;

const ModalContent = styled.div`
  background: white;
  border-radius: 16px;
  padding: 24px;
  width: 90%;
  max-width: 600px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
`;

const ModalHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
`;

const Title = styled.h2`
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #333;
`;

const CloseButton = styled.button`
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  padding: 4px;
  
  &:hover {
    color: #333;
  }
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

const TextArea = styled.textarea`
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  min-height: 100px;
  resize: vertical;
  font-family: inherit;
  transition: border-color 0.2s ease;

  &:focus {
    outline: none;
    border-color: #1976d2;
  }
`;

const Select = styled.select`
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  background: white;
  cursor: pointer;
  transition: border-color 0.2s ease;

  &:focus {
    outline: none;
    border-color: #1976d2;
  }
`;

const ImageUploadArea = styled.div`
  border: 2px dashed #ddd;
  border-radius: 8px;
  padding: 24px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    border-color: #1976d2;
    background: #f8f9fa;
  }
`;

const ImagePreview = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 12px;
  margin-top: 16px;
`;

const ImageItem = styled.div`
  position: relative;
  border-radius: 8px;
  overflow: hidden;
`;

const Image = styled.img`
  width: 100%;
  height: 120px;
  object-fit: cover;
`;

const RemoveButton = styled.button`
  position: absolute;
  top: 4px;
  right: 4px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  border: none;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  cursor: pointer;
  font-size: 12px;
`;

const TagInput = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 8px;
  min-height: 48px;
`;

const Tag = styled.span`
  background: #e3f2fd;
  color: #1976d2;
  padding: 4px 8px;
  border-radius: 16px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
`;

const TagRemoveButton = styled.button`
  background: none;
  border: none;
  color: #1976d2;
  cursor: pointer;
  font-size: 14px;
`;

const ButtonGroup = styled.div`
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 24px;
`;

const Button = styled.button<{ variant?: 'primary' | 'secondary' }>`
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;

  ${props => props.variant === 'primary' ? `
    background: #1976d2;
    color: white;
    
    &:hover {
      background: #1565c0;
    }
    
    &:disabled {
      background: #ccc;
      cursor: not-allowed;
    }
  ` : `
    background: #f5f5f5;
    color: #333;
    
    &:hover {
      background: #e0e0e0;
    }
  `}
`;

const MemoryModal: React.FC<MemoryModalProps> = ({
  isOpen,
  onClose,
  location,
  memory,
  groups,
  onMemoryCreated,
  onMemoryUpdated
}) => {
  const [images, setImages] = useState<File[]>([]);
  const [imagePreviews, setImagePreviews] = useState<string[]>([]);
  const [tags, setTags] = useState<string[]>([]);
  const [newTag, setNewTag] = useState('');
  const queryClient = useQueryClient();

  const { register, handleSubmit, reset, setValue } = useForm<CreateMemoryRequest>({
    defaultValues: {
      title: '',
      description: '',
      latitude: location?.lat || 0,
      longitude: location?.lng || 0,
      visitedAt: new Date().toISOString().split('T')[0],
      tagNames: []
    }
  });

  const createMemoryMutation = useMutation(memoryApi.createMemory, {
    onSuccess: () => {
      queryClient.invalidateQueries('memories');
      onMemoryCreated();
      handleClose();
    },
    onError: (error: any) => {
      toast.error('메모리 생성에 실패했습니다.');
      console.error('Create memory error:', error);
    }
  });

  const updateMemoryMutation = useMutation(
    (data: Partial<CreateMemoryRequest>) => memoryApi.updateMemory(memory!.id, data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries('memories');
        onMemoryUpdated();
        handleClose();
      },
      onError: (error: any) => {
        toast.error('메모리 수정에 실패했습니다.');
        console.error('Update memory error:', error);
      }
    }
  );

  useEffect(() => {
    if (memory) {
      setValue('title', memory.title);
      setValue('description', memory.description || '');
      setValue('latitude', memory.latitude);
      setValue('longitude', memory.longitude);
      setValue('visitedAt', memory.visitedAt.split('T')[0]);
      setValue('groupId', memory.groupId);
      setTags(memory.tags.map(tag => tag.name));
    } else if (location) {
      setValue('latitude', location.lat);
      setValue('longitude', location.lng);
    }
  }, [memory, location, setValue]);

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(e.target.files || []);
    const newImages = [...images, ...files];
    setImages(newImages);

    // 미리보기 생성
    files.forEach(file => {
      const reader = new FileReader();
      reader.onload = (e) => {
        setImagePreviews(prev => [...prev, e.target?.result as string]);
      };
      reader.readAsDataURL(file);
    });
  };

  const removeImage = (index: number) => {
    const newImages = images.filter((_, i) => i !== index);
    const newPreviews = imagePreviews.filter((_, i) => i !== index);
    setImages(newImages);
    setImagePreviews(newPreviews);
  };

  const addTag = () => {
    if (newTag.trim() && !tags.includes(newTag.trim())) {
      setTags([...tags, newTag.trim()]);
      setNewTag('');
    }
  };

  const removeTag = (tagToRemove: string) => {
    setTags(tags.filter(tag => tag !== tagToRemove));
  };

  const onSubmit = async (data: CreateMemoryRequest) => {
    if (!data.groupId) {
      toast.error('그룹을 선택해주세요.');
      return;
    }

    const submitData = {
      ...data,
      images,
      tagNames: tags
    };

    if (memory) {
      updateMemoryMutation.mutate(submitData);
    } else {
      createMemoryMutation.mutate(submitData);
    }
  };

  const handleClose = () => {
    reset();
    setImages([]);
    setImagePreviews([]);
    setTags([]);
    setNewTag('');
    onClose();
  };

  if (!isOpen) return null;

  return (
    <ModalOverlay onClick={handleClose}>
      <ModalContent onClick={(e) => e.stopPropagation()}>
        <ModalHeader>
          <Title>{memory ? '메모리 수정' : '새 메모리 추가'}</Title>
          <CloseButton onClick={handleClose}>×</CloseButton>
        </ModalHeader>

        <Form onSubmit={handleSubmit(onSubmit)}>
          <FormGroup>
            <Label>그룹 *</Label>
            <Select {...register('groupId', { required: true })}>
              <option value="">그룹을 선택하세요</option>
              {groups.map(group => (
                <option key={group.id} value={group.id}>
                  {group.name}
                </option>
              ))}
            </Select>
          </FormGroup>

          <FormGroup>
            <Label>제목 *</Label>
            <Input
              {...register('title', { required: true })}
              placeholder="메모리 제목을 입력하세요"
            />
          </FormGroup>

          <FormGroup>
            <Label>설명</Label>
            <TextArea
              {...register('description')}
              placeholder="메모리에 대한 설명을 입력하세요"
            />
          </FormGroup>

          <FormGroup>
            <Label>방문 날짜 *</Label>
            <Input
              type="date"
              {...register('visitedAt', { required: true })}
            />
          </FormGroup>

          <FormGroup>
            <Label>이미지</Label>
            <ImageUploadArea onClick={() => document.getElementById('image-upload')?.click()}>
              <input
                id="image-upload"
                type="file"
                multiple
                accept="image/*"
                onChange={handleImageUpload}
                style={{ display: 'none' }}
              />
              <div>
                <div style={{ fontSize: '24px', marginBottom: '8px' }}>📷</div>
                <div>이미지를 업로드하려면 클릭하세요</div>
                <div style={{ fontSize: '12px', color: '#666', marginTop: '4px' }}>
                  여러 이미지를 선택할 수 있습니다
                </div>
              </div>
            </ImageUploadArea>
            
            {imagePreviews.length > 0 && (
              <ImagePreview>
                {imagePreviews.map((preview, index) => (
                  <ImageItem key={index}>
                    <Image src={preview} alt={`Preview ${index}`} />
                    <RemoveButton onClick={() => removeImage(index)}>
                      ×
                    </RemoveButton>
                  </ImageItem>
                ))}
              </ImagePreview>
            )}
          </FormGroup>

          <FormGroup>
            <Label>태그</Label>
            <TagInput>
              {tags.map((tag, index) => (
                <Tag key={index}>
                  {tag}
                  <TagRemoveButton onClick={() => removeTag(tag)}>
                    ×
                  </TagRemoveButton>
                </Tag>
              ))}
              <input
                type="text"
                value={newTag}
                onChange={(e) => setNewTag(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && (e.preventDefault(), addTag())}
                placeholder="태그 입력 후 Enter"
                style={{
                  border: 'none',
                  outline: 'none',
                  background: 'transparent',
                  flex: 1,
                  minWidth: '100px'
                }}
              />
            </TagInput>
          </FormGroup>

          <ButtonGroup>
            <Button type="button" onClick={handleClose}>
              취소
            </Button>
            <Button
              type="submit"
              variant="primary"
              disabled={createMemoryMutation.isLoading || updateMemoryMutation.isLoading}
            >
              {memory ? '수정' : '생성'}
            </Button>
          </ButtonGroup>
        </Form>
      </ModalContent>
    </ModalOverlay>
  );
};

export default MemoryModal;