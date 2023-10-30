export interface Post {
  createdAt: string;
  postId: number;
  type: string;
  title: string;
  userId: number;
  body: string;
  open: boolean;
}

export interface PostListProps {
  type: string;
}

export interface AuthPost {
  createdAt: string;
  postId: number;
  title: string;
  userId: number;
  body: string;
  open: boolean;
  imageUrl: string;
}
